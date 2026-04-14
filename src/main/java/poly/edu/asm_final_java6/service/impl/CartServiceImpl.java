package poly.edu.asm_final_java6.service.impl;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import poly.edu.asm_final_java6.dto.CartItemDTO;
import poly.edu.asm_final_java6.entity.Product;
import poly.edu.asm_final_java6.service.CartService;
import poly.edu.asm_final_java6.service.ProductService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private static final String CART_SESSION_KEY = "cart";

    private final ProductService productService;

    @Override
    public void addToCart(HttpSession session, Long productId, int quantity, String size, String color) {
        List<CartItemDTO> cart = getCart(session);

        // Check if same product + size + color already exists
        for (CartItemDTO item : cart) {
            if (item.getProductId().equals(productId)
                    && Objects.equals(item.getSize(), size)
                    && Objects.equals(item.getColor(), color)) {
                item.setQuantity(item.getQuantity() + quantity);
                session.setAttribute(CART_SESSION_KEY, cart);
                return;
            }
        }

        // New item — lookup product info
        Product product = productService.getProductById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));

        CartItemDTO newItem = CartItemDTO.builder()
                .productId(product.getId())
                .productName(product.getName())
                .productImage(product.getImage())
                .price(product.getPrice())
                .quantity(quantity)
                .size(size)
                .color(color)
                .build();

        cart.add(newItem);
        session.setAttribute(CART_SESSION_KEY, cart);
    }

    @Override
    public void removeFromCart(HttpSession session, Long productId) {
        List<CartItemDTO> cart = getCart(session);
        cart.removeIf(item -> item.getProductId().equals(productId));
        session.setAttribute(CART_SESSION_KEY, cart);
    }

    @Override
    public void updateQuantity(HttpSession session, Long productId, int quantity) {
        List<CartItemDTO> cart = getCart(session);
        for (CartItemDTO item : cart) {
            if (item.getProductId().equals(productId)) {
                if (quantity <= 0) {
                    cart.remove(item);
                } else {
                    item.setQuantity(quantity);
                }
                break;
            }
        }
        session.setAttribute(CART_SESSION_KEY, cart);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<CartItemDTO> getCart(HttpSession session) {
        Object cart = session.getAttribute(CART_SESSION_KEY);
        if (cart instanceof List<?>) {
            return (List<CartItemDTO>) cart;
        }
        return new ArrayList<>();
    }

    @Override
    public BigDecimal getCartTotal(HttpSession session) {
        return getCart(session).stream()
                .map(CartItemDTO::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public int getCartItemCount(HttpSession session) {
        return getCart(session).stream()
                .mapToInt(CartItemDTO::getQuantity)
                .sum();
    }

    @Override
    public void clearCart(HttpSession session) {
        session.removeAttribute(CART_SESSION_KEY);
    }
}

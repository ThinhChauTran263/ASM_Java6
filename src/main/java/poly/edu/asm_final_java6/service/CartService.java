package poly.edu.asm_final_java6.service;

import jakarta.servlet.http.HttpSession;
import poly.edu.asm_final_java6.dto.CartItemDTO;

import java.math.BigDecimal;
import java.util.List;

public interface CartService {

    void addToCart(HttpSession session, Long productId, int quantity, String size, String color);

    void removeFromCart(HttpSession session, Long productId);

    void updateQuantity(HttpSession session, Long productId, int quantity);

    List<CartItemDTO> getCart(HttpSession session);

    BigDecimal getCartTotal(HttpSession session);

    int getCartItemCount(HttpSession session);

    void clearCart(HttpSession session);
}

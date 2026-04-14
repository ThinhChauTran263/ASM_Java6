package poly.edu.asm_final_java6.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import poly.edu.asm_final_java6.dto.CartItemDTO;
import poly.edu.asm_final_java6.dto.CheckoutRequest;
import poly.edu.asm_final_java6.entity.Order;
import poly.edu.asm_final_java6.entity.OrderDetail;
import poly.edu.asm_final_java6.entity.Product;
import poly.edu.asm_final_java6.entity.User;
import poly.edu.asm_final_java6.repository.OrderDetailRepository;
import poly.edu.asm_final_java6.repository.OrderRepository;
import poly.edu.asm_final_java6.repository.ProductRepository;
import poly.edu.asm_final_java6.service.OrderService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public Order placeOrder(User user, CheckoutRequest request, List<CartItemDTO> cartItems) {
        if (cartItems == null || cartItems.isEmpty()) {
            throw new IllegalArgumentException("Giỏ hàng trống, không thể đặt hàng");
        }

        Order order = Order.builder()
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .address(request.getAddress())
                .paymentMethod(request.getPaymentMethod())
                .discountCode(request.getDiscountCode())
                .user(user)
                .build();

        for (CartItemDTO item : cartItems) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Sản phẩm không tồn tại: " + item.getProductId()));

            OrderDetail detail = OrderDetail.builder()
                    .product(product)
                    .quantity(item.getQuantity())
                    .price(item.getPrice())
                    .size(item.getSize())
                    .color(item.getColor())
                    .build();

            order.addOrderDetail(detail);
        }

        order.setTotalAmount(order.calculateTotal());

        return orderRepository.save(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> getUserOrders(Long userId) {
        return orderRepository.findByUserIdOrderByCreatedDateDesc(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Order> getOrderById(Long orderId, Long userId) {
        return orderRepository.findByIdAndUserId(orderId, userId);
    }

    @Override
    @Transactional
    public boolean cancelOrder(Long orderId, Long userId) {
        Optional<Order> optionalOrder = orderRepository.findByIdAndUserId(orderId, userId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            if (order.canBeCancelled()) {
                order.cancel();
                orderRepository.save(order);
                return true;
            }
        }
        return false;
    }
}

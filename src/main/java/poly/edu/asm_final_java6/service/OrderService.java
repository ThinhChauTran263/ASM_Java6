package poly.edu.asm_final_java6.service;

import poly.edu.asm_final_java6.dto.CartItemDTO;
import poly.edu.asm_final_java6.dto.CheckoutRequest;
import poly.edu.asm_final_java6.entity.Order;
import poly.edu.asm_final_java6.entity.User;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    Order placeOrder(User user, CheckoutRequest request, List<CartItemDTO> cartItems);

    List<Order> getUserOrders(Long userId);

    Optional<Order> getOrderById(Long orderId, Long userId);

    boolean cancelOrder(Long orderId, Long userId);
}

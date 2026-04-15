package poly.edu.asm_final_java6.controller.api;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import poly.edu.asm_final_java6.dto.ApiResponse;
import poly.edu.asm_final_java6.dto.CartItemDTO;
import poly.edu.asm_final_java6.dto.CheckoutRequest;
import poly.edu.asm_final_java6.dto.OrderResponse;
import poly.edu.asm_final_java6.entity.Order;
import poly.edu.asm_final_java6.entity.User;
import poly.edu.asm_final_java6.security.CustomUserDetails;
import poly.edu.asm_final_java6.service.CartService;
import poly.edu.asm_final_java6.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderRestController {

    private final OrderService orderService;
    private final CartService cartService;

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getUserOrders(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }

        User user = userDetails.getUser();
        List<Order> orders = orderService.getUserOrders(user.getId());
        List<OrderResponse> response = orders.stream()
                .map(OrderResponse::from)
                .toList();

        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetail(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id) {

        if (userDetails == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }

        User user = userDetails.getUser();
        return orderService.getOrderById(id, user.getId())
                .<ResponseEntity<?>>map(order -> ResponseEntity.ok(ApiResponse.ok(OrderResponse.from(order))))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> placeOrder(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody CheckoutRequest checkoutRequest,
            HttpSession session) {

        if (userDetails == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }

        List<CartItemDTO> cartItems = cartService.getCart(session);
        if (cartItems.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Giỏ hàng trống"));
        }

        User user = userDetails.getUser();
        Order order = orderService.placeOrder(user, checkoutRequest, cartItems);
        cartService.clearCart(session);

        return ResponseEntity.ok(ApiResponse.ok("Đặt hàng thành công! Mã đơn hàng: #" + order.getId(),
                OrderResponse.from(order)));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<?>> cancelOrder(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id) {

        if (userDetails == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }

        User user = userDetails.getUser();
        boolean cancelled = orderService.cancelOrder(id, user.getId());

        if (cancelled) {
            return ResponseEntity.ok(ApiResponse.ok("Đã hủy đơn hàng #" + id + " thành công", null));
        } else {
            return ResponseEntity.badRequest().body(ApiResponse.error("Không thể hủy đơn hàng #" + id));
        }
    }
}

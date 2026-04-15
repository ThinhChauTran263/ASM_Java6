package poly.edu.asm_final_java6.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import poly.edu.asm_final_java6.entity.Order;
import poly.edu.asm_final_java6.entity.OrderDetail;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {

    private Long id;
    private String fullName;
    private String phone;
    private String email;
    private String address;
    private String paymentMethod;
    private BigDecimal totalAmount;
    private String status;
    private LocalDateTime createdDate;
    private boolean canBeCancelled;
    private List<OrderItemResponse> items;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OrderItemResponse {
        private Long productId;
        private String productName;
        private String productImage;
        private BigDecimal price;
        private Integer quantity;
        private BigDecimal subtotal;
    }

    public static OrderResponse from(Order order) {
        List<OrderItemResponse> items = order.getOrderDetails().stream()
                .map(OrderResponse::mapOrderDetail)
                .toList();

        return OrderResponse.builder()
                .id(order.getId())
                .fullName(order.getFullName())
                .phone(order.getPhone())
                .email(order.getEmail())
                .address(order.getAddress())
                .paymentMethod(order.getPaymentMethod().getDisplayName())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus().name())
                .createdDate(order.getCreatedDate())
                .canBeCancelled(order.canBeCancelled())
                .items(items)
                .build();
    }

    private static OrderItemResponse mapOrderDetail(OrderDetail detail) {
        return OrderItemResponse.builder()
                .productId(detail.getProduct() != null ? detail.getProduct().getId() : null)
                .productName(detail.getProduct() != null ? detail.getProduct().getName() : null)
                .productImage(detail.getProduct() != null ? detail.getProduct().getImage() : null)
                .price(detail.getPrice())
                .quantity(detail.getQuantity())
                .subtotal(detail.getSubtotal())
                .build();
    }
}

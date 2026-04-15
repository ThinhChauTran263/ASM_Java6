package poly.edu.asm_final_java6.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import poly.edu.asm_final_java6.entity.Product;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private String image;
    private Boolean available;
    private String badge;
    private String shortDescription;
    private String categoryName;
    private Long categoryId;
    private boolean hasDiscount;
    private BigDecimal discountPercentage;

    public static ProductResponse from(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .originalPrice(product.getOriginalPrice())
                .image(product.getImage())
                .available(product.getAvailable())
                .badge(product.getBadge())
                .shortDescription(product.getShortDescription())
                .categoryName(product.getCategory() != null ? product.getCategory().getName() : null)
                .categoryId(product.getCategory() != null ? product.getCategory().getId() : null)
                .hasDiscount(product.hasDiscount())
                .discountPercentage(product.getDiscountPercentage())
                .build();
    }
}

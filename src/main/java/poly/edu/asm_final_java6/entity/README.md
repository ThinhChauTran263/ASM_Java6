# Entity Layer - Clean Code Improvements

## Tổng quan các cải tiến

Các entity đã được tối ưu theo clean code principles với các cải tiến sau:

### 1. **Enums cho constants**
- `OrderStatus`: PENDING, CONFIRMED, SHIPPING, DELIVERED, CANCELLED
- `PaymentMethod`: CARD, COD, BANK

### 2. **Validation annotations**
- `@NotBlank`, `@NotNull`: Đảm bảo dữ liệu bắt buộc
- `@Email`: Validate email format
- `@Size`: Giới hạn độ dài string
- `@Min`: Validate số dương

### 3. **BigDecimal thay vì Double**
- Chính xác hơn cho tính toán tiền tệ
- Tránh lỗi làm tròn floating point

### 4. **Database indexes**
- Tối ưu query performance
- Index trên foreign keys và các trường thường query

### 5. **Fetch type optimization**
- `FetchType.LAZY` cho relationships
- Tránh N+1 query problem

### 6. **Business methods**
- `Product.hasDiscount()`, `getDiscountPercentage()`
- `Order.calculateTotal()`, `confirm()`, `cancel()`
- `OrderDetail.getSubtotal()`

### 7. **Helper methods cho relationships**
- `User.addOrder()`
- `Category.addProduct()`
- `Product.addImage()`
- `Order.addOrderDetail()`

### 8. **Loại bỏ @Data annotation**
- Thay bằng `@Getter` và `@Setter`
- Tránh vấn đề với circular references trong `toString()`, `equals()`, `hashCode()`

### 9. **Column definitions**
- Giới hạn độ dài cụ thể thay vì NVARCHAR(MAX)
- Precision và scale cho BigDecimal

### 10. **ArrayList initialization**
- `@Builder.Default` với `new ArrayList<>()`
- Tránh NullPointerException

## Entity Relationships

```
User (1) ----< (N) Order
Category (1) ----< (N) Product
Product (1) ----< (N) ProductImage
Product (1) ----< (N) OrderDetail
Order (1) ----< (N) OrderDetail
```

## Migration Notes

Nếu database đã có data, cần tạo migration script để:
1. Convert `Double` → `BigDecimal` (price, totalAmount)
2. Convert `String` → `Enum` (status, paymentMethod)
3. Thêm indexes mới
4. Update column lengths

## Dependencies Added

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

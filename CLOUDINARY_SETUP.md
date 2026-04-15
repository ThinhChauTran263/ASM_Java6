# Hướng dẫn cấu hình Cloudinary

## 1. Đăng ký tài khoản Cloudinary

1. Truy cập: https://cloudinary.com/users/register/free
2. Đăng ký tài khoản miễn phí
3. Sau khi đăng nhập, vào Dashboard để lấy thông tin:
   - Cloud Name
   - API Key
   - API Secret

## 2. Cấu hình credentials

Mở file `.env` và thay thế các giá trị sau:

```env
CLOUDINARY_CLOUD_NAME=your_cloud_name_here
CLOUDINARY_API_KEY=your_api_key_here
CLOUDINARY_API_SECRET=your_api_secret_here
```

## 3. Cài đặt dependencies

Chạy lệnh sau để tải Cloudinary dependency:

```bash
mvn clean install
```

## 4. Khởi động ứng dụng

```bash
mvn spring-boot:run
```

## 5. Test API Upload

### Upload ảnh (folder mặc định: asm_java6)

```bash
curl -X POST http://localhost:8080/api/images/upload \
  -F "file=@/path/to/your/image.jpg"
```

### Upload ảnh vào folder cụ thể

```bash
curl -X POST http://localhost:8080/api/images/upload/products \
  -F "file=@/path/to/your/image.jpg"
```

### Xóa ảnh (sử dụng public_id)

```bash
curl -X DELETE http://localhost:8080/api/images/asm_java6_your-image-id
```

## 6. Response Format

### Success Response

```json
{
  "success": true,
  "message": "Image uploaded successfully",
  "data": {
    "public_id": "asm_java6/uuid-here",
    "url": "https://res.cloudinary.com/your-cloud/image/upload/v1234567890/asm_java6/uuid-here.jpg",
    "secure_url": "https://res.cloudinary.com/your-cloud/image/upload/v1234567890/asm_java6/uuid-here.jpg",
    "format": "jpg",
    "width": 1920,
    "height": 1080,
    "bytes": 245678,
    "created_at": "2024-01-01T00:00:00Z"
  }
}
```

### Error Response

```json
{
  "success": false,
  "message": "File must be an image",
  "data": null
}
```

## 7. Sử dụng trong code

### Inject CloudinaryService

```java
@RestController
@RequiredArgsConstructor
public class ProductController {
    
    private final CloudinaryService cloudinaryService;
    
    @PostMapping("/products")
    public ResponseEntity<?> createProduct(
            @RequestParam("name") String name,
            @RequestParam("image") MultipartFile image) throws IOException {
        
        // Upload image to Cloudinary
        Map<String, Object> uploadResult = cloudinaryService.uploadImageToFolder(image, "products");
        
        // Get image URL
        String imageUrl = (String) uploadResult.get("secure_url");
        String publicId = (String) uploadResult.get("public_id");
        
        // Save product with imageUrl and publicId
        // ...
        
        return ResponseEntity.ok("Product created");
    }
}
```

## 8. Các tính năng đã cấu hình

✅ Upload ảnh lên Cloudinary
✅ Upload ảnh vào folder cụ thể
✅ Xóa ảnh từ Cloudinary
✅ Validate file type (chỉ cho phép image)
✅ Giới hạn file size: 10MB
✅ Tự động generate unique public_id
✅ Secure URL (HTTPS)
✅ Logging upload/delete operations

## 9. Cloudinary Features

Cloudinary cung cấp nhiều tính năng mạnh mẽ:

- **Image Transformation**: Resize, crop, rotate, format conversion
- **Optimization**: Tự động optimize ảnh cho web
- **CDN**: Phân phối ảnh qua CDN toàn cầu
- **Backup**: Tự động backup ảnh
- **Free tier**: 25 GB storage, 25 GB bandwidth/month

### Ví dụ Image Transformation

```
# Original
https://res.cloudinary.com/demo/image/upload/sample.jpg

# Resize to 300x300
https://res.cloudinary.com/demo/image/upload/w_300,h_300,c_fill/sample.jpg

# Convert to WebP
https://res.cloudinary.com/demo/image/upload/f_webp/sample.jpg

# Quality 80%
https://res.cloudinary.com/demo/image/upload/q_80/sample.jpg
```

## 10. Troubleshooting

### Lỗi: "Invalid API credentials"
- Kiểm tra lại CLOUDINARY_CLOUD_NAME, CLOUDINARY_API_KEY, CLOUDINARY_API_SECRET trong file .env
- Đảm bảo không có khoảng trắng thừa

### Lỗi: "File size exceeds maximum"
- Tăng giới hạn trong application-local.properties:
  ```properties
  spring.servlet.multipart.max-file-size=20MB
  spring.servlet.multipart.max-request-size=20MB
  ```

### Lỗi: "File must be an image"
- Chỉ chấp nhận file có MIME type bắt đầu bằng "image/"
- Các format được hỗ trợ: JPG, PNG, GIF, WebP, SVG, etc.

## 11. Best Practices

1. **Lưu public_id vào database**: Để có thể xóa ảnh sau này
2. **Sử dụng folder**: Tổ chức ảnh theo folder (products, users, categories, etc.)
3. **Validate file size**: Kiểm tra kích thước file trước khi upload
4. **Handle errors**: Xử lý lỗi upload và thông báo cho user
5. **Delete old images**: Xóa ảnh cũ khi user upload ảnh mới
6. **Use secure_url**: Luôn sử dụng HTTPS URL

## 12. Next Steps

- [ ] Tích hợp upload ảnh vào Product management
- [ ] Tích hợp upload avatar cho User profile
- [ ] Thêm image preview trước khi upload
- [ ] Thêm progress bar cho upload
- [ ] Implement multiple images upload
- [ ] Add image cropping/editing UI

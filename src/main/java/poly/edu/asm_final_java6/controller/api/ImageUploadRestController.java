package poly.edu.asm_final_java6.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import poly.edu.asm_final_java6.dto.ApiResponse;
import poly.edu.asm_final_java6.service.CloudinaryService;

import java.util.Map;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
@Slf4j
public class ImageUploadRestController {

    private final CloudinaryService cloudinaryService;

    /**
     * Upload single image
     * POST /api/images/upload
     */
    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<Map<String, Object>>> uploadImage(
            @RequestParam("file") MultipartFile file) {
        try {
            Map<String, Object> uploadResult = cloudinaryService.uploadImage(file);
            
            return ResponseEntity.ok(ApiResponse.<Map<String, Object>>builder()
                    .success(true)
                    .message("Image uploaded successfully")
                    .data(uploadResult)
                    .build());

        } catch (IllegalArgumentException e) {
            log.error("Invalid file: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.<Map<String, Object>>builder()
                    .success(false)
                    .message(e.getMessage())
                    .build());

        } catch (Exception e) {
            log.error("Failed to upload image", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<Map<String, Object>>builder()
                            .success(false)
                            .message("Failed to upload image: " + e.getMessage())
                            .build());
        }
    }

    /**
     * Upload image to specific folder
     * POST /api/images/upload/{folder}
     */
    @PostMapping("/upload/{folder}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> uploadImageToFolder(
            @RequestParam("file") MultipartFile file,
            @PathVariable String folder) {
        try {
            Map<String, Object> uploadResult = cloudinaryService.uploadImageToFolder(file, folder);
            
            return ResponseEntity.ok(ApiResponse.<Map<String, Object>>builder()
                    .success(true)
                    .message("Image uploaded successfully to folder: " + folder)
                    .data(uploadResult)
                    .build());

        } catch (IllegalArgumentException e) {
            log.error("Invalid file: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.<Map<String, Object>>builder()
                    .success(false)
                    .message(e.getMessage())
                    .build());

        } catch (Exception e) {
            log.error("Failed to upload image to folder: {}", folder, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<Map<String, Object>>builder()
                            .success(false)
                            .message("Failed to upload image: " + e.getMessage())
                            .build());
        }
    }

    /**
     * Delete image by public_id
     * DELETE /api/images/{publicId}
     */
    @DeleteMapping("/{publicId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> deleteImage(
            @PathVariable String publicId) {
        try {
            // Replace URL-encoded slash with actual slash
            String decodedPublicId = publicId.replace("_", "/");
            
            Map<String, Object> deleteResult = cloudinaryService.deleteImage(decodedPublicId);
            
            return ResponseEntity.ok(ApiResponse.<Map<String, Object>>builder()
                    .success(true)
                    .message("Image deleted successfully")
                    .data(deleteResult)
                    .build());

        } catch (Exception e) {
            log.error("Failed to delete image: {}", publicId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<Map<String, Object>>builder()
                            .success(false)
                            .message("Failed to delete image: " + e.getMessage())
                            .build());
        }
    }
}

package poly.edu.asm_final_java6.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import poly.edu.asm_final_java6.service.CloudinaryService;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudinaryServiceImpl implements CloudinaryService {

    private final Cloudinary cloudinary;

    @Override
    public Map<String, Object> uploadImage(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        // Validate file type
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("File must be an image");
        }

        try {
            // Generate unique public_id
            String publicId = UUID.randomUUID().toString();

            Map<String, Object> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "public_id", publicId,
                            "resource_type", "image",
                            "folder", "asm_java6"
                    )
            );

            log.info("Image uploaded successfully: {}", uploadResult.get("url"));
            return uploadResult;

        } catch (IOException e) {
            log.error("Failed to upload image to Cloudinary", e);
            throw new IOException("Failed to upload image: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> deleteImage(String publicId) throws IOException {
        try {
            Map<String, Object> deleteResult = cloudinary.uploader().destroy(
                    publicId,
                    ObjectUtils.emptyMap()
            );

            log.info("Image deleted successfully: {}", publicId);
            return deleteResult;

        } catch (IOException e) {
            log.error("Failed to delete image from Cloudinary: {}", publicId, e);
            throw new IOException("Failed to delete image: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> uploadImageToFolder(MultipartFile file, String folder) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        // Validate file type
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("File must be an image");
        }

        try {
            // Generate unique public_id
            String publicId = UUID.randomUUID().toString();

            Map<String, Object> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "public_id", publicId,
                            "resource_type", "image",
                            "folder", folder
                    )
            );

            log.info("Image uploaded successfully to folder {}: {}", folder, uploadResult.get("url"));
            return uploadResult;

        } catch (IOException e) {
            log.error("Failed to upload image to Cloudinary folder: {}", folder, e);
            throw new IOException("Failed to upload image: " + e.getMessage(), e);
        }
    }
}

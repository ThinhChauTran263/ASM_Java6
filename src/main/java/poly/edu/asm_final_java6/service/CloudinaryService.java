package poly.edu.asm_final_java6.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface CloudinaryService {
    /**
     * Upload image to Cloudinary
     * @param file MultipartFile to upload
     * @return Map containing upload result (url, public_id, etc.)
     * @throws IOException if upload fails
     */
    Map<String, Object> uploadImage(MultipartFile file) throws IOException;

    /**
     * Delete image from Cloudinary
     * @param publicId Public ID of the image to delete
     * @return Map containing deletion result
     * @throws IOException if deletion fails
     */
    Map<String, Object> deleteImage(String publicId) throws IOException;

    /**
     * Upload image to specific folder in Cloudinary
     * @param file MultipartFile to upload
     * @param folder Folder name in Cloudinary
     * @return Map containing upload result
     * @throws IOException if upload fails
     */
    Map<String, Object> uploadImageToFolder(MultipartFile file, String folder) throws IOException;
}

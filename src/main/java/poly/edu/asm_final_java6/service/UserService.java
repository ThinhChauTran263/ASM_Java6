package poly.edu.asm_final_java6.service;

import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;
import poly.edu.asm_final_java6.dto.ChangePasswordRequest;
import poly.edu.asm_final_java6.dto.ProfileUpdateRequest;
import poly.edu.asm_final_java6.entity.User;

public interface UserService {

    Optional<User> getUserById(Long id);

    User updateProfile(Long userId, ProfileUpdateRequest request);

    User updateAvatar(Long userId, MultipartFile avatarFile);

    void changePassword(Long userId, ChangePasswordRequest request);
}

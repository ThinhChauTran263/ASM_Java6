package poly.edu.asm_final_java6.service.impl;

import java.io.IOException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import poly.edu.asm_final_java6.dto.ChangePasswordRequest;
import poly.edu.asm_final_java6.dto.ProfileUpdateRequest;
import poly.edu.asm_final_java6.entity.User;
import poly.edu.asm_final_java6.repository.UserRepository;
import poly.edu.asm_final_java6.service.CloudinaryService;
import poly.edu.asm_final_java6.service.UserService;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional
    public User updateProfile(Long userId, ProfileUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("Không tìm thấy người dùng với ID: " + userId));

        user.setFullName(request.getFullName().trim());
        user.setPhone(request.getPhone() != null ? request.getPhone().trim() : null);
        user.setAddress(request.getAddress() != null ? request.getAddress().trim() : null);

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User updateAvatar(Long userId, MultipartFile avatarFile) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("Không tìm thấy người dùng với ID: " + userId));

        try {
            Map<String, Object> uploadResult = cloudinaryService.uploadImageToFolder(avatarFile, "asm_java6/avatars");
            String imageUrl = (String) uploadResult.get("secure_url");
            user.setImageUrl(imageUrl);
            log.info("Avatar updated for user {}: {}", userId, imageUrl);
            return userRepository.save(user);
        } catch (IOException e) {
            log.error("Failed to upload avatar for user {}", userId, e);
            throw new RuntimeException("Không thể tải ảnh đại diện lên. Vui lòng thử lại.", e);
        }
    }

    @Override
    @Transactional
    public void changePassword(Long userId, ChangePasswordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("Không tìm thấy người dùng với ID: " + userId));

        if (user.getPassword() == null) {
            throw new IllegalArgumentException("Tài khoản liên kết OAuth2 không thể đổi mật khẩu tại đây.");
        }

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Mật khẩu hiện tại không đúng.");
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Mật khẩu mới và xác nhận mật khẩu không khớp.");
        }

        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Mật khẩu mới không được trùng với mật khẩu hiện tại.");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        log.info("Password changed for user {}", userId);
    }
}

package poly.edu.asm_final_java6.service.impl;

import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import poly.edu.asm_final_java6.dto.LoginRequest;
import poly.edu.asm_final_java6.dto.RegisterRequest;
import poly.edu.asm_final_java6.entity.User;
import poly.edu.asm_final_java6.entity.enums.Role;
import poly.edu.asm_final_java6.repository.UserRepository;
import poly.edu.asm_final_java6.service.AuthService;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User register(RegisterRequest request) {
        String normalizedEmail = normalizeEmail(request.getEmail());
        if (normalizedEmail == null || normalizedEmail.isBlank()) {
            throw new IllegalArgumentException("Email khong duoc de trong");
        }
        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new IllegalArgumentException("Email da duoc dang ky");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new IllegalArgumentException("Mat khau khong hop le");
        }
        if (!Objects.equals(request.getPassword(), request.getConfirmPassword())) {
            throw new IllegalArgumentException("Mat khau xac nhan khong trung khop");
        }

        String fullName = request.getFullName() == null ? "" : request.getFullName().trim();

        User user = User.builder()
            .fullName(fullName)
            .email(normalizedEmail)
            .password(passwordEncoder.encode(request.getPassword()))
            .role(Role.USER)
            .build();

        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> authenticate(LoginRequest request) {
        String normalizedEmail = normalizeEmail(request.getEmail());
        if (normalizedEmail == null || normalizedEmail.isBlank() || request.getPassword() == null) {
            return Optional.empty();
        }

        return userRepository.findByEmail(normalizedEmail)
            .filter(user -> user.getPassword() != null
                && passwordEncoder.matches(request.getPassword(), user.getPassword()));
    }

    private String normalizeEmail(String email) {
        if (email == null) {
            return null;
        }
        return email.trim().toLowerCase();
    }
}

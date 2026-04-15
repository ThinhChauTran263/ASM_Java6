package poly.edu.asm_final_java6.controller.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import poly.edu.asm_final_java6.dto.ApiResponse;
import poly.edu.asm_final_java6.dto.ProfileUpdateRequest;
import poly.edu.asm_final_java6.entity.User;
import poly.edu.asm_final_java6.security.CustomUserDetails;
import poly.edu.asm_final_java6.service.UserService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<?>> getProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }

        User user = userDetails.getUser();
        Map<String, Object> data = new HashMap<>();
        data.put("id", user.getId());
        data.put("fullName", user.getFullName());
        data.put("email", user.getEmail());
        data.put("phone", user.getPhone());
        data.put("address", user.getAddress());
        data.put("imageUrl", user.getImageUrl());

        return ResponseEntity.ok(ApiResponse.ok(data));
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<?>> updateProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody ProfileUpdateRequest request) {

        if (userDetails == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }

        User user = userDetails.getUser();
        User updated = userService.updateProfile(user.getId(), request);

        Map<String, Object> data = new HashMap<>();
        data.put("id", updated.getId());
        data.put("fullName", updated.getFullName());
        data.put("email", updated.getEmail());
        data.put("phone", updated.getPhone());
        data.put("address", updated.getAddress());

        return ResponseEntity.ok(ApiResponse.ok("Cập nhật thông tin thành công", data));
    }
}

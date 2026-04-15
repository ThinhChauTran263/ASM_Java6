package poly.edu.asm_final_java6.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import poly.edu.asm_final_java6.dto.ChangePasswordRequest;
import poly.edu.asm_final_java6.dto.ProfileUpdateRequest;
import poly.edu.asm_final_java6.entity.User;
import poly.edu.asm_final_java6.security.CustomUserDetails;
import poly.edu.asm_final_java6.service.UserService;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal CustomUserDetails userDetails,
                          Model model) {
        User user = userDetails.getUser();
        ProfileUpdateRequest profileForm = ProfileUpdateRequest.builder()
                .fullName(user.getFullName())
                .phone(user.getPhone())
                .address(user.getAddress())
                .build();

        model.addAttribute("profileForm", profileForm);
        model.addAttribute("changePasswordForm", new ChangePasswordRequest());
        model.addAttribute("user", user);
        return "ui/profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@AuthenticationPrincipal CustomUserDetails userDetails,
                                @Valid @ModelAttribute("profileForm") ProfileUpdateRequest profileForm,
                                BindingResult bindingResult,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", userDetails.getUser());
            model.addAttribute("changePasswordForm", new ChangePasswordRequest());
            return "ui/profile";
        }

        User user = userDetails.getUser();
        userService.updateProfile(user.getId(), profileForm);

        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thông tin thành công!");
        return "redirect:/profile";
    }

    @PostMapping("/profile/avatar")
    public String updateAvatar(@AuthenticationPrincipal CustomUserDetails userDetails,
                               @RequestParam("avatarFile") MultipartFile avatarFile,
                               RedirectAttributes redirectAttributes) {
        if (avatarFile.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng chọn ảnh để tải lên.");
            return "redirect:/profile";
        }

        String contentType = avatarFile.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            redirectAttributes.addFlashAttribute("errorMessage", "File phải là ảnh (jpg, png, gif,...).");
            return "redirect:/profile";
        }

        if (avatarFile.getSize() > 5 * 1024 * 1024) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ảnh không được vượt quá 5MB.");
            return "redirect:/profile";
        }

        try {
            User user = userDetails.getUser();
            User updatedUser = userService.updateAvatar(user.getId(), avatarFile);
            userDetails.getUser().setImageUrl(updatedUser.getImageUrl());
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật ảnh đại diện thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi tải ảnh: " + e.getMessage());
        }

        return "redirect:/profile";
    }

    @PostMapping("/profile/change-password")
    public String changePassword(@AuthenticationPrincipal CustomUserDetails userDetails,
                                 @Valid @ModelAttribute("changePasswordForm") ChangePasswordRequest changePasswordForm,
                                 BindingResult bindingResult,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            User user = userDetails.getUser();
            model.addAttribute("user", user);
            model.addAttribute("profileForm", ProfileUpdateRequest.builder()
                    .fullName(user.getFullName())
                    .phone(user.getPhone())
                    .address(user.getAddress())
                    .build());
            model.addAttribute("showPasswordSection", true);
            return "ui/profile";
        }

        try {
            userService.changePassword(userDetails.getUser().getId(), changePasswordForm);
            redirectAttributes.addFlashAttribute("successMessage", "Đổi mật khẩu thành công!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/profile";
    }
}

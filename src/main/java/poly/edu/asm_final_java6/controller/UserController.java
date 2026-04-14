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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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
            return "ui/profile";
        }

        User user = userDetails.getUser();
        userService.updateProfile(user.getId(), profileForm);

        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thông tin thành công!");
        return "redirect:/profile";
    }
}

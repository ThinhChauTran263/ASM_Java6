package poly.edu.asm_final_java6.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import poly.edu.asm_final_java6.dto.SessionUser;
import poly.edu.asm_final_java6.entity.Category;
import poly.edu.asm_final_java6.security.CustomUserDetails;
import poly.edu.asm_final_java6.service.CartService;
import poly.edu.asm_final_java6.service.CategoryService;

import java.util.List;

@ControllerAdvice
@RequiredArgsConstructor
public class CartAdvice {

    private final CartService cartService;
    private final CategoryService categoryService;

    @ModelAttribute("cartItemCount")
    public int cartItemCount(HttpSession session) {
        return cartService.getCartItemCount(session);
    }

    @ModelAttribute("currentUser")
    public SessionUser currentUser(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            return SessionUser.from(userDetails.getUser());
        }
        return null;
    }

    @ModelAttribute("categories")
    public List<Category> categories() {
        return categoryService.getAllCategories();
    }
}

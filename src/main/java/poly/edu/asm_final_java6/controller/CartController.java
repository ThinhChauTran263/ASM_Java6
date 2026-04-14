package poly.edu.asm_final_java6.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import poly.edu.asm_final_java6.service.CartService;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public String cart(HttpSession session, Model model) {
        model.addAttribute("cartItems", cartService.getCart(session));
        model.addAttribute("cartTotal", cartService.getCartTotal(session));
        model.addAttribute("cartItemCount", cartService.getCartItemCount(session));
        return "ui/cart";
    }

    @PostMapping("/add")
    public String addToCart(HttpSession session,
                            @RequestParam Long productId,
                            @RequestParam(defaultValue = "1") int quantity,
                            @RequestParam(required = false) String size,
                            @RequestParam(required = false) String color) {
        cartService.addToCart(session, productId, quantity, size, color);
        return "redirect:/cart";
    }

    @PostMapping("/remove")
    public String removeFromCart(HttpSession session,
                                 @RequestParam Long productId) {
        cartService.removeFromCart(session, productId);
        return "redirect:/cart";
    }

    @PostMapping("/update")
    public String updateQuantity(HttpSession session,
                                  @RequestParam Long productId,
                                  @RequestParam int quantity) {
        cartService.updateQuantity(session, productId, quantity);
        return "redirect:/cart";
    }
}

package poly.edu.asm_final_java6.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UiController {

    @GetMapping({"/", "/home", "/home.html"})
    public String home() {
        return "ui/home";
    }

    @GetMapping({"/login", "/login.html"})
    public String login() {
        return "ui/login";
    }

    @GetMapping({"/register", "/register.html"})
    public String register() {
        return "ui/register";
    }

    @GetMapping({"/product", "/product-detail-linked", "/product-detail-linked.html"})
    public String productDetail() {
        return "ui/product-detail-linked";
    }

    @GetMapping({"/cart", "/cart.html"})
    public String cart() {
        return "ui/cart";
    }

    @GetMapping({"/checkout", "/checkout.html"})
    public String checkout() {
        return "ui/checkout";
    }
}

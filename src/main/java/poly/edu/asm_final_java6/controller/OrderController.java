package poly.edu.asm_final_java6.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import poly.edu.asm_final_java6.dto.CartItemDTO;
import poly.edu.asm_final_java6.dto.CheckoutRequest;
import poly.edu.asm_final_java6.entity.Order;
import poly.edu.asm_final_java6.entity.User;
import poly.edu.asm_final_java6.entity.enums.PaymentMethod;
import poly.edu.asm_final_java6.security.CustomUserDetails;
import poly.edu.asm_final_java6.service.CartService;
import poly.edu.asm_final_java6.service.OrderService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final CartService cartService;

    @GetMapping("/checkout")
    public String checkout(@AuthenticationPrincipal CustomUserDetails userDetails,
                           HttpSession session,
                           Model model) {
        User user = userDetails.getUser();
        List<CartItemDTO> cartItems = cartService.getCart(session);

        if (cartItems.isEmpty()) {
            return "redirect:/cart";
        }

        CheckoutRequest checkoutRequest = CheckoutRequest.builder()
                .fullName(user.getFullName())
                .phone(user.getPhone())
                .email(user.getEmail())
                .address(user.getAddress())
                .build();

        model.addAttribute("checkoutRequest", checkoutRequest);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("cartTotal", cartService.getCartTotal(session));
        model.addAttribute("paymentMethods", PaymentMethod.values());
        return "ui/checkout";
    }

    @PostMapping("/checkout")
    public String placeOrder(@AuthenticationPrincipal CustomUserDetails userDetails,
                             @Valid CheckoutRequest checkoutRequest,
                             BindingResult bindingResult,
                             HttpSession session,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        List<CartItemDTO> cartItems = cartService.getCart(session);

        if (cartItems.isEmpty()) {
            return "redirect:/cart";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("cartItems", cartItems);
            model.addAttribute("cartTotal", cartService.getCartTotal(session));
            model.addAttribute("paymentMethods", PaymentMethod.values());
            return "ui/checkout";
        }

        User user = userDetails.getUser();
        Order order = orderService.placeOrder(user, checkoutRequest, cartItems);
        cartService.clearCart(session);

        redirectAttributes.addFlashAttribute("successMessage",
                "Đặt hàng thành công! Mã đơn hàng: #" + order.getId());
        return "redirect:/orders";
    }

    @GetMapping("/orders")
    public String orderHistory(@AuthenticationPrincipal CustomUserDetails userDetails,
                               Model model) {
        User user = userDetails.getUser();
        List<Order> orders = orderService.getUserOrders(user.getId());
        model.addAttribute("orders", orders);
        return "ui/order-history";
    }

    @PostMapping("/orders/{id}/cancel")
    public String cancelOrder(@AuthenticationPrincipal CustomUserDetails userDetails,
                              @PathVariable Long id,
                              RedirectAttributes redirectAttributes) {
        User user = userDetails.getUser();
        boolean cancelled = orderService.cancelOrder(id, user.getId());

        if (cancelled) {
            redirectAttributes.addFlashAttribute("successMessage", "Đã hủy đơn hàng #" + id + " thành công.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Không thể hủy đơn hàng #" + id + ".");
        }

        return "redirect:/orders";
    }
}

package poly.edu.asm_final_java6.controller.api;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import poly.edu.asm_final_java6.dto.ApiResponse;
import poly.edu.asm_final_java6.dto.CartItemDTO;
import poly.edu.asm_final_java6.service.CartService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartRestController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getCart(HttpSession session) {
        List<CartItemDTO> items = cartService.getCart(session);
        BigDecimal total = cartService.getCartTotal(session);
        int count = cartService.getCartItemCount(session);

        Map<String, Object> data = new HashMap<>();
        data.put("items", items);
        data.put("total", total);
        data.put("itemCount", count);

        return ResponseEntity.ok(ApiResponse.ok(data));
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<?>> addToCart(
            HttpSession session,
            @RequestParam Long productId,
            @RequestParam(defaultValue = "1") int quantity,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String color) {

        cartService.addToCart(session, productId, quantity, size, color);

        Map<String, Object> data = new HashMap<>();
        data.put("items", cartService.getCart(session));
        data.put("total", cartService.getCartTotal(session));
        data.put("itemCount", cartService.getCartItemCount(session));

        return ResponseEntity.ok(ApiResponse.ok("Đã thêm sản phẩm vào giỏ hàng", data));
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<?>> updateQuantity(
            HttpSession session,
            @RequestParam Long productId,
            @RequestParam int quantity) {

        if (quantity < 1) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Số lượng phải >= 1"));
        }

        cartService.updateQuantity(session, productId, quantity);

        Map<String, Object> data = new HashMap<>();
        data.put("items", cartService.getCart(session));
        data.put("total", cartService.getCartTotal(session));
        data.put("itemCount", cartService.getCartItemCount(session));

        return ResponseEntity.ok(ApiResponse.ok("Đã cập nhật số lượng", data));
    }

    @DeleteMapping("/remove")
    public ResponseEntity<ApiResponse<?>> removeFromCart(
            HttpSession session,
            @RequestParam Long productId) {

        cartService.removeFromCart(session, productId);

        Map<String, Object> data = new HashMap<>();
        data.put("items", cartService.getCart(session));
        data.put("total", cartService.getCartTotal(session));
        data.put("itemCount", cartService.getCartItemCount(session));

        return ResponseEntity.ok(ApiResponse.ok("Đã xóa sản phẩm khỏi giỏ hàng", data));
    }
}

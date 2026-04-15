package poly.edu.asm_final_java6.controller;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import poly.edu.asm_final_java6.dto.RegisterRequest;
import poly.edu.asm_final_java6.entity.Product;
import poly.edu.asm_final_java6.service.CategoryService;
import poly.edu.asm_final_java6.service.ProductService;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UiController {

    private final ProductService productService;
    private final CategoryService categoryService;

    @GetMapping({"/", "/home", "/home.html"})
    public String home(@RequestParam(required = false) Long categoryId,
                       @RequestParam(required = false) String keyword,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "12") int size,
                       Model model) {
        
        // Debug authentication
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info("=== HOME PAGE ACCESS ===");
        log.info("Authentication: {}", auth != null ? auth.getName() : "null");
        log.info("Is Authenticated: {}", auth != null && auth.isAuthenticated());
        log.info("Principal Type: {}", auth != null ? auth.getPrincipal().getClass().getName() : "null");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        Page<Product> products;
        if (keyword != null && !keyword.isBlank()) {
            products = productService.searchProducts(keyword.trim(), pageable);
            model.addAttribute("keyword", keyword.trim());
        } else if (categoryId != null) {
            products = productService.getProductsByCategory(categoryId, pageable);
            model.addAttribute("selectedCategoryId", categoryId);
        } else {
            products = productService.getAvailableProducts(pageable);
        }

        model.addAttribute("products", products);
        return "ui/index";
    }

    @GetMapping({"/login", "/login.html"})
    public String login() {
        return "ui/login";
    }

    @GetMapping({"/register", "/register.html"})
    public String register(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "ui/register";
    }

    @GetMapping({"/product", "/product-detail-linked", "/product-detail-linked.html"})
    public String productDetail(@RequestParam Long id, Model model) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + id));
        model.addAttribute("product", product);
        return "ui/product-detail-linked";
    }

    @GetMapping({"/products", "/product-list", "/product-list.html"})
    public String productList(@RequestParam(required = false) Long categoryId,
                              @RequestParam(required = false) String keyword,
                              @RequestParam(required = false) BigDecimal minPrice,
                              @RequestParam(required = false) BigDecimal maxPrice,
                              @RequestParam(defaultValue = "id") String sortBy,
                              @RequestParam(defaultValue = "desc") String sortDir,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "12") int size,
                              Model model) {
        
        Sort sort = sortDir.equalsIgnoreCase("asc") 
            ? Sort.by(sortBy).ascending() 
            : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Product> products;
        
        // Lọc theo keyword và category
        if (keyword != null && !keyword.isBlank()) {
            products = productService.searchProducts(keyword.trim(), pageable);
            model.addAttribute("keyword", keyword.trim());
        } else if (categoryId != null) {
            products = productService.getProductsByCategory(categoryId, pageable);
            model.addAttribute("selectedCategoryId", categoryId);
        } else {
            products = productService.getAvailableProducts(pageable);
        }

        // Lọc theo giá (client-side filtering vì không có method trong service)
        if (minPrice != null || maxPrice != null) {
            products = products.map(product -> {
                if (minPrice != null && product.getPrice().compareTo(minPrice) < 0) {
                    return null;
                }
                if (maxPrice != null && product.getPrice().compareTo(maxPrice) > 0) {
                    return null;
                }
                return product;
            });
        }

        model.addAttribute("products", products);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        
        return "ui/product-list";
    }

}

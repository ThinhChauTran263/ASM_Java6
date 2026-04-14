package poly.edu.asm_final_java6.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import poly.edu.asm_final_java6.repository.UserRepository;
import poly.edu.asm_final_java6.security.CustomUserDetails;

@Slf4j
@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

    private final UserRepository userRepository;

    @GetMapping("/auth")
    public String testAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        log.info("=== TEST AUTH ===");
        log.info("Authenticated: {}", auth != null && auth.isAuthenticated());
        log.info("Principal: {}", auth != null ? auth.getPrincipal() : "null");
        log.info("Name: {}", auth != null ? auth.getName() : "null");
        
        if (auth != null && auth.isAuthenticated()) {
            return "Authenticated as: " + auth.getName();
        }
        return "Not authenticated";
    }
    
    @GetMapping("/user-info")
    public String testUserInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        log.info("=== TEST USER INFO ===");
        if (auth != null && auth.isAuthenticated()) {
            log.info("Principal type: {}", auth.getPrincipal().getClass().getName());
            
            if (auth.getPrincipal() instanceof CustomUserDetails) {
                CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
                log.info("User ID: {}", userDetails.getUser().getId());
                log.info("User Email: {}", userDetails.getUser().getEmail());
                log.info("User Name: {}", userDetails.getUser().getFullName());
                
                // Check if user exists in database
                boolean existsInDb = userRepository.existsByEmail(userDetails.getUser().getEmail());
                log.info("User exists in DB: {}", existsInDb);
                
                return String.format("User: %s (ID: %d), Exists in DB: %s", 
                    userDetails.getUser().getFullName(), 
                    userDetails.getUser().getId(),
                    existsInDb);
            }
        }
        return "No user info available";
    }
    
    @GetMapping("/home-redirect")
    public String testHomeRedirect() {
        log.info("=== TEST HOME REDIRECT ===");
        return "redirect:/home";
    }
}

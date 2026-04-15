package poly.edu.asm_final_java6.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    public CustomAuthenticationSuccessHandler() {
        setDefaultTargetUrl("/home");
        setAlwaysUseDefaultTargetUrl(false); // Allow saved request to work
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws ServletException, IOException {
        
        log.info("=== Authentication Success ===");
        log.info("User: {}", authentication.getName());
        log.info("Principal: {}", authentication.getPrincipal().getClass().getName());
        log.info("Authorities: {}", authentication.getAuthorities());
        
        // Clear any existing session attributes that might cause issues
        HttpSession session = request.getSession(false);
        if (session != null) {
            log.info("Session ID: {}", session.getId());
        }
        
        // Redirect admin users to admin dashboard
        var authorities = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        if (authorities.contains("ROLE_ADMIN")) {
            log.info("Admin user detected, redirecting to /admin/dashboard");
            getRedirectStrategy().sendRedirect(request, response, "/admin/dashboard");
            return;
        }
        
        // Check if there's a saved request
        String targetUrl = determineTargetUrl(request, response, authentication);
        log.info("Target URL: {}", targetUrl);
        
        if (response.isCommitted()) {
            log.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}

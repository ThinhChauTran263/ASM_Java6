package poly.edu.asm_final_java6.security;

import java.util.Map;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import poly.edu.asm_final_java6.entity.User;
import poly.edu.asm_final_java6.entity.enums.Role;
import poly.edu.asm_final_java6.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);
        
        String provider = userRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes = oauth2User.getAttributes();
        
        log.info("OAuth2 Login - Provider: {}, Email: {}", provider, attributes.get("email"));
        
        User user = processOAuth2User(provider, attributes);
        
        log.info("User processed - ID: {}, Email: {}", user.getId(), user.getEmail());
        
        return new CustomUserDetails(user, attributes);
    }

    private User processOAuth2User(String provider, Map<String, Object> attributes) {
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        String picture = (String) attributes.get("picture");
        String providerId = (String) attributes.get("sub");

        if (email == null || email.isBlank()) {
            log.error("OAuth2 login failed - no email provided by {}", provider);
            throw new OAuth2AuthenticationException("Email is required for OAuth2 login");
        }
        email = email.trim().toLowerCase();

        log.info("Processing OAuth2 user - Email: {}, Name: {}, Provider: {}", email, name, provider);

        final String normalizedEmail = email;
        return userRepository.findByEmail(normalizedEmail)
                .map(existingUser -> {
                    log.info("Updating existing user: {}", normalizedEmail);
                    return updateExistingUser(existingUser, name, picture, provider, providerId);
                })
                .orElseGet(() -> {
                    log.info("Creating new user: {}", normalizedEmail);
                    return createNewUser(normalizedEmail, name, picture, provider, providerId);
                });
    }

    private User updateExistingUser(User user, String name, String picture, String provider, String providerId) {
        if (name != null && !name.isBlank()) {
            user.setFullName(name);
        }
        if (picture != null && !picture.isBlank()) {
            user.setImageUrl(picture);
        }
        if (user.getProvider() == null) {
            user.setProvider(provider);
            user.setProviderId(providerId);
        }
        User savedUser = userRepository.save(user);
        log.info("User updated successfully - ID: {}", savedUser.getId());
        return savedUser;
    }

    private User createNewUser(String email, String name, String picture, String provider, String providerId) {
        String fullName = (name != null && !name.isBlank()) ? name : email.split("@")[0];
        User newUser = User.builder()
                .email(email)
                .fullName(fullName)
                .imageUrl(picture)
                .provider(provider)
                .providerId(providerId)
                .role(Role.USER)
                .build();
        User savedUser = userRepository.save(newUser);
        log.info("New user created successfully - ID: {}, Email: {}", savedUser.getId(), savedUser.getEmail());
        return savedUser;
    }
}

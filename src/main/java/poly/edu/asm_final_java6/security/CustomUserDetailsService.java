package poly.edu.asm_final_java6.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import poly.edu.asm_final_java6.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("Form login attempt - Email: {}", email);
        
        return userRepository.findByEmail(email.toLowerCase().trim())
                .map(user -> {
                    log.info("User found - ID: {}, Email: {}, Has Password: {}", 
                            user.getId(), user.getEmail(), user.getPassword() != null);
                    return new CustomUserDetails(user);
                })
                .orElseThrow(() -> {
                    log.warn("User not found - Email: {}", email);
                    return new UsernameNotFoundException("Email hoặc mật khẩu không đúng");
                });
    }
}

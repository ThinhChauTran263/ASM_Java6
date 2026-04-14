package poly.edu.asm_final_java6.service;

import java.util.Optional;
import poly.edu.asm_final_java6.dto.LoginRequest;
import poly.edu.asm_final_java6.dto.RegisterRequest;
import poly.edu.asm_final_java6.entity.User;

public interface AuthService {

    User register(RegisterRequest request);

    Optional<User> authenticate(LoginRequest request);
}

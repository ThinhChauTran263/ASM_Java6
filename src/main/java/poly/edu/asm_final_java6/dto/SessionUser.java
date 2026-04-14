package poly.edu.asm_final_java6.dto;

import poly.edu.asm_final_java6.entity.User;

public record SessionUser(Long id, String fullName, String email) {

    public static SessionUser from(User user) {
        return new SessionUser(user.getId(), user.getFullName(), user.getEmail());
    }
}

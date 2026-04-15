package poly.edu.asm_final_java6.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import poly.edu.asm_final_java6.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);

    // --- User registration statistics ---
    @Query(value = "SELECT CAST(created_date AS DATE) AS label, COUNT(*) AS value FROM users GROUP BY CAST(created_date AS DATE) ORDER BY label", nativeQuery = true)
    List<Object[]> countUsersByDay();

    @Query(value = "SELECT TO_CHAR(created_date, 'YYYY-MM') AS label, COUNT(*) AS value FROM users GROUP BY TO_CHAR(created_date, 'YYYY-MM') ORDER BY label", nativeQuery = true)
    List<Object[]> countUsersByMonth();

    @Query(value = "SELECT TO_CHAR(created_date, 'YYYY') AS label, COUNT(*) AS value FROM users GROUP BY TO_CHAR(created_date, 'YYYY') ORDER BY label", nativeQuery = true)
    List<Object[]> countUsersByYear();
}

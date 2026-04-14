package poly.edu.asm_final_java6.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import poly.edu.asm_final_java6.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}

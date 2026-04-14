package poly.edu.asm_final_java6.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import poly.edu.asm_final_java6.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByAvailableTrue(Pageable pageable);

    Page<Product> findByCategoryIdAndAvailableTrue(Long categoryId, Pageable pageable);

    Page<Product> findByNameContainingIgnoreCaseAndAvailableTrue(String keyword, Pageable pageable);
}

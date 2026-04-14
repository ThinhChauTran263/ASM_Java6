package poly.edu.asm_final_java6.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import poly.edu.asm_final_java6.entity.Product;

public interface ProductService {

    Page<Product> getAvailableProducts(Pageable pageable);

    Optional<Product> getProductById(Long id);

    Page<Product> getProductsByCategory(Long categoryId, Pageable pageable);

    Page<Product> searchProducts(String keyword, Pageable pageable);
}

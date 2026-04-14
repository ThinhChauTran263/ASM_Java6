package poly.edu.asm_final_java6.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import poly.edu.asm_final_java6.entity.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
}

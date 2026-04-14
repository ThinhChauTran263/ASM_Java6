package poly.edu.asm_final_java6.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import poly.edu.asm_final_java6.entity.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserIdOrderByCreatedDateDesc(Long userId);

    Optional<Order> findByIdAndUserId(Long orderId, Long userId);
}

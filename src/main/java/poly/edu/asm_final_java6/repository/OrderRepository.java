package poly.edu.asm_final_java6.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import poly.edu.asm_final_java6.entity.Order;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserIdOrderByCreatedDateDesc(Long userId);

    Optional<Order> findByIdAndUserId(Long orderId, Long userId);

    // --- Order count statistics ---
    @Query(value = "SELECT CAST(created_date AS DATE) AS label, COUNT(*) AS value FROM orders GROUP BY CAST(created_date AS DATE) ORDER BY label", nativeQuery = true)
    List<Object[]> countOrdersByDay();

    @Query(value = "SELECT TO_CHAR(created_date, 'YYYY-MM') AS label, COUNT(*) AS value FROM orders GROUP BY TO_CHAR(created_date, 'YYYY-MM') ORDER BY label", nativeQuery = true)
    List<Object[]> countOrdersByMonth();

    @Query(value = "SELECT TO_CHAR(created_date, 'YYYY') AS label, COUNT(*) AS value FROM orders GROUP BY TO_CHAR(created_date, 'YYYY') ORDER BY label", nativeQuery = true)
    List<Object[]> countOrdersByYear();

    // --- Revenue statistics ---
    @Query(value = "SELECT CAST(created_date AS DATE) AS label, COALESCE(SUM(total_amount),0) AS value FROM orders WHERE status <> 'CANCELLED' GROUP BY CAST(created_date AS DATE) ORDER BY label", nativeQuery = true)
    List<Object[]> revenueByDay();

    @Query(value = "SELECT TO_CHAR(created_date, 'YYYY-MM') AS label, COALESCE(SUM(total_amount),0) AS value FROM orders WHERE status <> 'CANCELLED' GROUP BY TO_CHAR(created_date, 'YYYY-MM') ORDER BY label", nativeQuery = true)
    List<Object[]> revenueByMonth();

    @Query(value = "SELECT TO_CHAR(created_date, 'YYYY') AS label, COALESCE(SUM(total_amount),0) AS value FROM orders WHERE status <> 'CANCELLED' GROUP BY TO_CHAR(created_date, 'YYYY') ORDER BY label", nativeQuery = true)
    List<Object[]> revenueByYear();

    // Total revenue (non-cancelled)
    @Query(value = "SELECT COALESCE(SUM(total_amount),0) FROM orders WHERE status <> 'CANCELLED'", nativeQuery = true)
    BigDecimal totalRevenue();
}

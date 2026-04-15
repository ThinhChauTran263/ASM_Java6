package poly.edu.asm_final_java6.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import poly.edu.asm_final_java6.dto.ChartDataPoint;
import poly.edu.asm_final_java6.repository.OrderRepository;
import poly.edu.asm_final_java6.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/api/stats")
@RequiredArgsConstructor
public class AdminApiController {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @GetMapping("/revenue")
    public ResponseEntity<List<ChartDataPoint>> revenue(@RequestParam(defaultValue = "month") String period) {
        List<Object[]> raw = switch (period) {
            case "day" -> orderRepository.revenueByDay();
            case "year" -> orderRepository.revenueByYear();
            default -> orderRepository.revenueByMonth();
        };
        return ResponseEntity.ok(toChartData(raw));
    }

    @GetMapping("/orders")
    public ResponseEntity<List<ChartDataPoint>> orders(@RequestParam(defaultValue = "month") String period) {
        List<Object[]> raw = switch (period) {
            case "day" -> orderRepository.countOrdersByDay();
            case "year" -> orderRepository.countOrdersByYear();
            default -> orderRepository.countOrdersByMonth();
        };
        return ResponseEntity.ok(toChartData(raw));
    }

    @GetMapping("/users")
    public ResponseEntity<List<ChartDataPoint>> users(@RequestParam(defaultValue = "month") String period) {
        List<Object[]> raw = switch (period) {
            case "day" -> userRepository.countUsersByDay();
            case "year" -> userRepository.countUsersByYear();
            default -> userRepository.countUsersByMonth();
        };
        return ResponseEntity.ok(toChartData(raw));
    }

    private List<ChartDataPoint> toChartData(List<Object[]> rows) {
        return rows.stream()
                .map(r -> new ChartDataPoint(r[0].toString(), (Number) r[1]))
                .collect(Collectors.toList());
    }
}

package poly.edu.asm_final_java6.exception;

import java.nio.file.AccessDeniedException;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalArgument(IllegalArgumentException e, Model model) {
        log.warn("Bad request: {}", e.getMessage());
        model.addAttribute("status", 400);
        model.addAttribute("error", "Yêu cầu không hợp lệ");
        model.addAttribute("message", e.getMessage());
        return "error/404";
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(NoSuchElementException e, Model model) {
        log.warn("Not found: {}", e.getMessage());
        model.addAttribute("status", 404);
        model.addAttribute("error", "Không tìm thấy");
        model.addAttribute("message", e.getMessage());
        return "error/404";
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleAccessDenied(AccessDeniedException e, Model model) {
        log.warn("Access denied: {}", e.getMessage());
        model.addAttribute("status", 403);
        model.addAttribute("error", "Không có quyền truy cập");
        model.addAttribute("message", "Bạn không có quyền thực hiện thao tác này.");
        return "error/404";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException(Exception e, Model model) {
        log.error("Internal server error: {}", e.getMessage(), e);
        model.addAttribute("status", 500);
        model.addAttribute("error", "Lỗi hệ thống");
        model.addAttribute("message", "Đã xảy ra lỗi không mong muốn. Vui lòng thử lại sau.");
        return "error/500";
    }
}

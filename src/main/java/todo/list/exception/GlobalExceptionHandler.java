package todo.list.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import todo.list.api.dto.response.CommonResponse;
import todo.list.domain.account.log.ErrorLog;
import todo.list.domain.account.log.ErrorLogRepository;

@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final static String ADDRESS = "http://localhost:8080";

    private final ErrorLogRepository errorLogRepository;
    private final HttpServletRequest request;

    @ExceptionHandler(TodoListException.class)
    public ResponseEntity<CommonResponse> responseTodoListException(
        TodoListException exception) {
        errorLogRepository.save(
            ErrorLog.create(exception.getExceptionStatus().value(),
                request.getMethod(), exception.getExceptionStatus().name(),
                exception.getExceptionMessage(),
                request.getRequestURL().toString().replace(ADDRESS, "")
            )
        );
        return new ResponseEntity<>(
            CommonResponse.of(exception.getExceptionStatus(), exception.getExceptionMessage()),
            exception.getExceptionStatus());
    }
}

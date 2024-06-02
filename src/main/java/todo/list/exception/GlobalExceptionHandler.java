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
    private final ObjectMapper objectMapper;

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse> responseMethodArgumentNotValidException(
        MethodArgumentNotValidException e) throws JsonProcessingException {
        Map<String, String> error = new HashMap<>();
        e.getAllErrors().forEach(it -> {
            error.put(((FieldError) it).getField(), it.getDefaultMessage());
        });
        errorLogRepository.save(
            ErrorLog.create(400,
                request.getMethod(), "BAD_REQUEST",
                objectMapper.writeValueAsString(error),
                request.getRequestURL().toString().replace(ADDRESS, "")
            )
        );
        return new ResponseEntity<>(
            CommonResponse.badRequest(error), HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse> responseException(Exception e) {
        errorLogRepository.save(
            ErrorLog.create(500,
                request.getMethod(), "INTERNAL_SERVER_ERROR",
                e.getMessage(), request.getRequestURL().toString().replace(ADDRESS, "")
            )
        );
        request.getPathInfo(); //  /api/forms
        return new ResponseEntity<>(
            CommonResponse.serverError("서버 에러"), HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<CommonResponse> sQLIntegrityConstraintViolationException(
        SQLIntegrityConstraintViolationException e) {
        errorLogRepository.save(
            ErrorLog.create(400,
                request.getMethod(), "BAD_REQUEST",
                "무결성 제약 위반이 발생하였습니다", request.getRequestURL().toString().replace(ADDRESS, "")
            )
        );
        return new ResponseEntity<>(
            CommonResponse.badRequest("무결성 제약 위반이 발생하였습니다"), HttpStatus.BAD_REQUEST
        );
    }
}

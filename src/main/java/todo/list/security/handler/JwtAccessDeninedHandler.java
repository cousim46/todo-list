package todo.list.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import todo.list.api.controller.dto.response.CommonResponse;
import todo.list.domain.account.log.ErrorLog;
import todo.list.domain.account.log.ErrorLogRepository;

@Component
@RequiredArgsConstructor
public class JwtAccessDeninedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;
    private final ErrorLogRepository errorLogRepository;
    private static final String errorMessage = "접근 권한이 없습니다.";

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
        AccessDeniedException accessDeniedException) throws IOException, ServletException {
        errorLogRepository.save(ErrorLog.create(
            403, request.getMethod(), "FORBIDDEN", errorMessage,
            request.getRequestURI().toString()
        ));
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(
            CommonResponse.of(HttpStatus.FORBIDDEN, errorMessage)
        ));
        response.setStatus(403);
    }
}

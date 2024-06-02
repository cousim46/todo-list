package todo.list.domain.account.log;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class ErrorLogTest {

    @DisplayName("상태 코드값, 상태 코드, http 메서드, 에러메시지, 요청 경로를 통해 에러로그를 생성한다. ")
    @Test
    void create() {
        //given
        int statusCode = 400;
        String status = HttpStatus.BAD_REQUEST.name();
        String httpMethod = "POST";
        String message = "message";
        String requestPath = "/api/test";

        //when
        ErrorLog errorLog = ErrorLog.create(statusCode, httpMethod, status, message, requestPath);

        //then
        assertThat(errorLog.getErrorMessage()).isEqualTo(message);
        assertThat(errorLog.getStatus()).isEqualTo(status);
        assertThat(errorLog.getStatusCode()).isEqualTo(statusCode);
        assertThat(errorLog.getRequestPath()).isEqualTo(requestPath);
        assertThat(errorLog.getHttpMethod()).isEqualTo(httpMethod);
    }

}
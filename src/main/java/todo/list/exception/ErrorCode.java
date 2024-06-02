package todo.list.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    DUPLICATE_LOGIN_ID(HttpStatus.CONFLICT,"중복된 아이디가 존재합니다."),
    DUPLICATE_NICKNAME(HttpStatus.CONFLICT,"중복된 닉네임이 존재합니다.");

    private final HttpStatus status;
    private final String message;
}

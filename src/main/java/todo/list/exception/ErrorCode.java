package todo.list.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    //401
    EXPIRE_TOKEN_TIME(HttpStatus.UNAUTHORIZED,"토큰시간이 만료되었습니다."),

    //403
    NO_AUTHENTICATION(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),

    //404
    NOT_EXIST_ACCOUNT_INFO(HttpStatus.BAD_REQUEST,"회원이 존재하지 않습니다."),
    NOT_MATCH_PASSWORD_OR_ID(HttpStatus.BAD_REQUEST,"아이디 또는 비밀번호가 올바르지 않습니다."),

    //409
    DUPLICATE_LOGIN_ID(HttpStatus.CONFLICT,"중복된 아이디가 존재합니다."),
    DUPLICATE_NICKNAME(HttpStatus.CONFLICT,"중복된 닉네임이 존재합니다.");

    private final HttpStatus status;
    private final String message;
}

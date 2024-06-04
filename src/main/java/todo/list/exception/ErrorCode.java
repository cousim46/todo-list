package todo.list.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    //400
    NOT_MATCH_PASSWORD_OR_ID(HttpStatus.BAD_REQUEST,"아이디 또는 비밀번호가 올바르지 않습니다."),
    NOT_CHANGE_STATUS(HttpStatus.BAD_REQUEST, "대기 상태는 진행 상태일때만 상태 변경이 가능합니다."),

    //401
    EXPIRE_TOKEN_TIME(HttpStatus.UNAUTHORIZED,"토큰시간이 만료되었습니다."),

    //403
    NO_AUTHENTICATION(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),


    //404
    NOT_EXIST_TODO(HttpStatus.NOT_FOUND,"존재하지 않는 Todo입니다."),
    NOT_EXIST_ACCOUNT_INFO(HttpStatus.NOT_FOUND,"회원이 존재하지 않습니다."),
    NOT_EXIST_TOKEN_INFO(HttpStatus.BAD_REQUEST,"존재하지 않는 토큰 정보입니다."),

    //409
    DUPLICATE_LOGIN_ID(HttpStatus.CONFLICT,"중복된 아이디가 존재합니다."),
    DUPLICATE_NICKNAME(HttpStatus.CONFLICT,"중복된 닉네임이 존재합니다.");

    private final HttpStatus status;
    private final String message;
}

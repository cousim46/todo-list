package todo.list.api.controller.account.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import todo.list.token.response.TokenResponse;

public record TokenInfoResponse(
    Long id,
    String grantType,
    String accessToken,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime accessTokenExpireAt,
    String refreshToken,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime refreshTokenExpireAt
) {
    public static TokenInfoResponse of(TokenResponse tokenResponse) {
        return new TokenInfoResponse(
            tokenResponse.id(),
            tokenResponse.grantType(),
            tokenResponse.accessToken(),
            tokenResponse.accessTokenExpireAt(),
            tokenResponse.refreshToken(),
            tokenResponse.refreshTokenExpireAt()
            );
    }
}

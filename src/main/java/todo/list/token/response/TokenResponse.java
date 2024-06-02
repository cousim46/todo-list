package todo.list.token.response;

import java.time.LocalDateTime;

public record TokenResponse(
    Long id,
    String grantType,
    String accessToken,
    LocalDateTime accessTokenExpireAt,
    String refreshToken,
    LocalDateTime refreshTokenExpireAt
) { }

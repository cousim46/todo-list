package todo.list.token;


import static todo.list.exception.ErrorCode.NOT_EXIST_ACCOUNT_INFO;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import todo.list.domain.account.Account;
import todo.list.domain.account.AccountRepository;
import todo.list.domain.token.Token;
import todo.list.exception.TodoListException;
import todo.list.security.LoginUserDetail;
import todo.list.token.response.TokenResponse;

@Component
public class TokenProvider {

    private final TokenRepository tokenRepository;
    private final AccountRepository accountRepository;
    private final String secretKey;
    private final Key key;
    private static final int ACCESS_TOKEN_EXPIRE_TIME = 300;
    private static final int REFRESH_TOKEN_EXPIRE_TIME = 3600;
    private static final String GRANT_TYPE = "Bearer";

    public TokenProvider(TokenRepository tokenRepository, AccountRepository accountRepository,
        @Value("${jwt.secret-key}") String secretKey) {
        this.tokenRepository = tokenRepository;
        this.accountRepository = accountRepository;
        this.secretKey = secretKey;
        byte[] secretKeyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(secretKeyBytes);

    }

    public TokenResponse get(Long id, String role, LocalDateTime now) {
        LocalDateTime accessTokenExpireAt = now.plus(ACCESS_TOKEN_EXPIRE_TIME, ChronoUnit.SECONDS);
        LocalDateTime refreshTokenExpireAt = now.plus(REFRESH_TOKEN_EXPIRE_TIME,
            ChronoUnit.SECONDS);

        String accessToken = createToken(id,
            accessTokenExpireAt.toInstant(ZoneOffset.UTC).toEpochMilli(),
            role);
        String refreshToken = createToken(id,
            refreshTokenExpireAt.toInstant(ZoneOffset.UTC).toEpochMilli(),
            role);

        Token findToken = tokenRepository.findByAccountId(id)
            .orElse(null);
        if (findToken == null) {
            saveToken(id, accessToken, accessTokenExpireAt, refreshToken, refreshTokenExpireAt);
            return new TokenResponse(id, GRANT_TYPE, accessToken, accessTokenExpireAt, refreshToken,
                refreshTokenExpireAt);
        }

        updateTokenProcess(now, findToken, accessToken, accessTokenExpireAt, refreshToken,
            refreshTokenExpireAt);

        return new TokenResponse(id, GRANT_TYPE, findToken.getAccess(),
            findToken.getAccessExpireAt(), findToken.getRefresh(), findToken.getRefreshExpireAt());
    }

    private void updateTokenProcess(LocalDateTime now, Token findToken, String accessToken,
        LocalDateTime accessTokenExpireAt, String refreshToken,
        LocalDateTime refreshTokenExpireAt) {
        if (findToken.getAccessExpireAt().isBefore(now)) {
            if (findToken.getRefreshExpireAt().isBefore(now)) {
                updateToken(findToken, accessToken, accessTokenExpireAt, refreshToken,
                    refreshTokenExpireAt);
            } else {
                updateToken(findToken, accessToken, accessTokenExpireAt);
            }
        }
    }

    private void updateToken(Token token, String accessToken, LocalDateTime accessTokenExpireAt,
        String refreshToken, LocalDateTime refreshTokenExpireAt) {
        token.updateRefreshToken(refreshToken, refreshTokenExpireAt);
        token.updateAccessToken(accessToken, accessTokenExpireAt);
    }

    private void updateToken(Token token, String accessToken, LocalDateTime accessTokenExpireAt) {
        token.updateAccessToken(accessToken, accessTokenExpireAt);
    }

    private void saveToken(Long id, String accessToken, LocalDateTime accessTokenExpireAt,
        String refreshToken, LocalDateTime refreshTokenExpireAt) {
        Account account = accountRepository.findById(id)
            .orElseThrow(() -> new TodoListException(NOT_EXIST_ACCOUNT_INFO));
        tokenRepository.save(Token.create(accessToken, accessTokenExpireAt, account, refreshToken,
            refreshTokenExpireAt));
    }

    public Authentication getAuthentication(String token, LocalDateTime now) {
        Token findToken = tokenRepository.findTokenLoginInfo(token).orElse(null);

        if (findToken == null) {
            return null;
        } else if (now.isAfter(findToken.getAccessExpireAt())) {
            throw new JwtException("토큰이 만료되었습니다.");
        }
        Account account = accountRepository.findById(getAccount(token))
            .orElse(null);
        if (account == null) {
            return null;
        }
        LoginUserDetail loginUserDetail = new LoginUserDetail(account.getId(),
            account.getRole().name());
        return new UsernamePasswordAuthenticationToken(loginUserDetail, "",
            loginUserDetail.getAuthorities());
    }

    private Long getAccount(String token) {
        return Long.parseLong(Jwts.parserBuilder().setSigningKey(secretKey).build()
            .parseClaimsJws(token).getBody().getSubject());
    }

    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
            return true;
        } catch (SecurityException | UnsupportedJwtException | IllegalArgumentException e) {
            e.printStackTrace();
        } catch (ExpiredJwtException e) {
            return false;
        }
        return false;
    }

    public String createToken(Long id, Long tokenExpireTime, String role) {
        return Jwts.builder()
            .setSubject(id.toString())
            .claim("auth", role)
            .setExpiration(new Date(tokenExpireTime))
            .signWith(key, SignatureAlgorithm.HS512).compact();
    }
}
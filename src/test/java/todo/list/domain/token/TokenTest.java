package todo.list.domain.token;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import todo.list.domain.account.Account;

class TokenTest {

    @DisplayName("액세스 토큰, 만료시간과 리프레쉬 토큰, 만료시간, 회원으로 토큰을 생성한다.")
    @Test
    void create() {
        //given
        String nickname = "nickname";
        String loginId = "loginId";
        String password = "password";
        String salt = "salt";
        Account account = Account.signUp(nickname, loginId, password, salt);
        String access = "access";
        LocalDateTime accessExpireAt = LocalDateTime.now();
        String refresh = "refresh";
        LocalDateTime refreshExpireAt = LocalDateTime.now();

        //when
        Token token = Token.create(access, accessExpireAt, account, refresh,
            refreshExpireAt);

        //then
        assertThat(token.getAccess()).isEqualTo(access);
        assertThat(token.getRefresh()).isEqualTo(refresh);
        assertThat(token.getAccessExpireAt()).isEqualTo(accessExpireAt);
        assertThat(token.getRefreshExpireAt()).isEqualTo(refreshExpireAt);
    }

    @DisplayName("생성된 액세스 토큰의 값과 만료시간을 수정한다.")
    @Test
    void updateAccessToken() {
        //given
        String nickname = "nickname";
        String loginId = "loginId";
        String password = "password";
        String salt = "salt";
        Account account = Account.signUp(nickname, loginId, password, salt);
        String access = "access";
        LocalDateTime accessExpireAt = LocalDateTime.now();
        String refresh = "refresh";
        LocalDateTime refreshExpireAt = LocalDateTime.now();

        Token token = Token.create(access, accessExpireAt, account, refresh,
            refreshExpireAt);

        String updateAccess = "updateAccess";
        LocalDateTime updateAccessExpireAt = accessExpireAt.plusMinutes(5);

        //when
        token.updateAccessToken(updateAccess,updateAccessExpireAt);

        //then
        assertThat(token.getAccess()).isNotEqualTo(access);
        assertThat(token.getRefresh()).isEqualTo(refresh);
        assertThat(token.getAccessExpireAt()).isNotEqualTo(accessExpireAt);
        assertThat(token.getRefreshExpireAt()).isEqualTo(refreshExpireAt);
        assertThat(token.getAccess()).isEqualTo(updateAccess);
        assertThat(token.getAccessExpireAt()).isEqualTo(updateAccessExpireAt);
    }
    @DisplayName("생성된 리프레쉬 토큰의 값과 만료시간을 수정한다.")
    @Test
    void updateRefreshToken() {
        //given
        String nickname = "nickname";
        String loginId = "loginId";
        String password = "password";
        String salt = "salt";
        Account account = Account.signUp(nickname, loginId, password, salt);
        String access = "access";
        LocalDateTime accessExpireAt = LocalDateTime.now();
        String refresh = "refresh";
        LocalDateTime refreshExpireAt = LocalDateTime.now();

        Token token = Token.create(access, accessExpireAt, account, refresh,
            refreshExpireAt);

        String updateRefresh = "updateRefresh";
        LocalDateTime updateRefreshExpireAt = refreshExpireAt.plusMinutes(5);

        //when
        token.updateRefreshToken(updateRefresh,updateRefreshExpireAt);

        //then
        assertThat(token.getAccess()).isEqualTo(access);
        assertThat(token.getRefresh()).isNotEqualTo(refresh);
        assertThat(token.getAccessExpireAt()).isEqualTo(accessExpireAt);
        assertThat(token.getRefreshExpireAt()).isNotEqualTo(refreshExpireAt);
        assertThat(token.getRefresh()).isEqualTo(updateRefresh);
        assertThat(token.getRefreshExpireAt()).isEqualTo(updateRefreshExpireAt);
    }
}
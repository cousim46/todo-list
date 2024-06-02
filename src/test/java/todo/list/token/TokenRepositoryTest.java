package todo.list.token;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import todo.list.config.AuditingConfig;
import todo.list.domain.account.Account;
import todo.list.domain.account.AccountRepository;
import todo.list.domain.token.Token;

@DataJpaTest
@Import(value = AuditingConfig.class)
class TokenRepositoryTest {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TokenRepository tokenRepository;

    @DisplayName("회원id 값으로 토큰을 찾을 수 있다.")
    @Test
    void findByAccountId() {
        //given
        String nickname = "nickname";
        String loginId = "loginId";
        String password = "password";
        String salt = "salt";
        Account account = Account.signUp(nickname, loginId, password, salt);
        Account savedAccount = accountRepository.save(account);

        Token token = Token.create("access", LocalDateTime.now(), savedAccount, "refresh",
            LocalDateTime.now());
        tokenRepository.save(token);

        //when
        Token findToken = tokenRepository.findByAccountId(savedAccount.getId()).get();

        //then
        assertThat(findToken.getAccess()).isEqualTo(token.getAccess());
        assertThat(findToken.getRefresh()).isEqualTo(token.getRefresh());
        assertThat(findToken.getAccessExpireAt()).isEqualTo(token.getAccessExpireAt());
        assertThat(findToken.getRefreshExpireAt()).isEqualTo(token.getRefreshExpireAt());
    }
    @DisplayName("액세스 토큰으로 토큰을 찾을 수 있다.")
    @Test
    void findTokenLoginInfo() {
        //given
        String nickname = "nickname";
        String loginId = "loginId";
        String password = "password";
        String salt = "salt";
        Account account = Account.signUp(nickname, loginId, password, salt);
        Account savedAccount = accountRepository.save(account);

        Token token = Token.create("access", LocalDateTime.now(), savedAccount, "refresh",
            LocalDateTime.now());
        Token savedToken = tokenRepository.save(token);

        //when
        Token findToken = tokenRepository.findTokenLoginInfo(savedToken.getAccess()).get();

        //then
        assertThat(findToken.getAccess()).isEqualTo(token.getAccess());
        assertThat(findToken.getRefresh()).isEqualTo(token.getRefresh());
        assertThat(findToken.getAccessExpireAt()).isEqualTo(token.getAccessExpireAt());
        assertThat(findToken.getRefreshExpireAt()).isEqualTo(token.getRefreshExpireAt());
    }
}
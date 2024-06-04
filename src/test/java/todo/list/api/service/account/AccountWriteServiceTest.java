package todo.list.api.service.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import todo.list.domain.account.Account;
import todo.list.domain.account.AccountRepository;
import todo.list.domain.token.Token;
import todo.list.exception.TodoListException;
import todo.list.token.TokenRepository;
import todo.list.token.response.TokenResponse;

@SpringBootTest
class AccountWriteServiceTest {

    @Autowired
    private AccountWriteService accountWriteService;

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TokenRepository tokenRepository;

    @AfterEach
    void clear() {
        tokenRepository.deleteAllInBatch();
        accountRepository.deleteAllInBatch();
    }

    @DisplayName("로그인 아이디, 비밀번호, 닉네임, salt으로 회원을 생성한다.")
    @Test
    void signUp() {
        //given
        String nickname = "nickname";
        String loginId = "loginId";
        String password = "password";
        String salt = "salt";

        //when
        accountWriteService.signUp(loginId, nickname, password, salt);

        //then
        List<Account> findAllAccounts = accountRepository.findAll();
        assertThat(findAllAccounts)
            .hasSize(1)
            .extracting("nickname", "loginId")
            .contains(
                tuple(nickname, loginId)
            );
    }

    @DisplayName("이미 가입된 아이디가 존재하면 예외가 발생한다.")
    @Test
    void occurExistLoginIdException() {
        //given
        String nickname = "nickname";
        String loginId = "loginId";
        String password = "password";
        String salt = "salt";
        accountRepository.save(Account.signUp(nickname, loginId, password, salt));
        String inputNickname = "nickname2";
        //when
        TodoListException todoListException = assertThrows(TodoListException.class,
            () -> accountWriteService.signUp(loginId, inputNickname, password, salt));

        //then
        assertThat(todoListException.getExceptionMessage()).isEqualTo("중복된 아이디가 존재합니다.");
        assertThat(todoListException.getExceptionStatus()).isEqualTo(HttpStatus.CONFLICT);
    }


    @DisplayName("이미 가입된 닉네임이 존재하면 예외가 발생한다.")
    @Test
    void occurExistNicknameException() {
        //given
        String nickname = "nickname";
        String loginId = "loginId";
        String password = "password";
        String salt = "salt";
        accountRepository.save(Account.signUp(nickname, loginId, password, salt));
        String inputLoginId = "loginId2";
        //when
        TodoListException todoListException = assertThrows(TodoListException.class,
            () -> accountWriteService.signUp(inputLoginId, nickname, password, salt));

        //then
        assertThat(todoListException.getExceptionMessage()).isEqualTo("중복된 닉네임이 존재합니다.");
        assertThat(todoListException.getExceptionStatus()).isEqualTo(HttpStatus.CONFLICT);
    }

    @DisplayName("로그인 성공 시 토큰을 발급한다.")
    @Test
    void successLogin() {
        //given
        String loginId = "loginId";
        String nickname = "nickname";
        String password = "password";
        String salt = "1234";
        LocalDateTime now = LocalDateTime.of(2024, 6, 2, 12, 0, 0);
        accountWriteService.signUp(loginId, nickname, password, salt);
        //when
        TokenResponse token = accountWriteService.login(loginId, password, now);

        //then
        List<Account> accounts = accountRepository.findAll();
        List<Token> tokens = tokenRepository.findAll();

        assertThat(accounts)
            .hasSize(1)
            .extracting("loginId", "nickname")
            .contains(
                tuple(loginId, nickname)
            );
        assertThat(tokens)
            .hasSize(1)
            .extracting("access", "accessExpireAt", "refresh", "refreshExpireAt")
            .contains(
                tuple(token.accessToken(), token.accessTokenExpireAt(), token.refreshToken(),
                    token.refreshTokenExpireAt())
            );


    }

    @DisplayName("로그인하려는 아이디가 틀릴 경우 예외가 발생한다.")
    @Test
    void occurWrongLoginIdException() {
        //given
        String loginId = "loginId";
        String nickname = "nickname";
        String password = "password";
        String salt = "1234";
        accountRepository.save(Account.signUp(nickname, loginId, password, salt));
        String wrongLoginId = "test";
        LocalDateTime now = LocalDateTime.now();

        //when
        TodoListException todoListException = assertThrows(TodoListException.class,
            () -> accountWriteService.login(wrongLoginId, password, now));

        //then
        assertThat(todoListException.getExceptionStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(todoListException.getExceptionMessage()).isEqualTo("아이디 또는 비밀번호가 올바르지 않습니다.");
    }

    @DisplayName("로그인 하려는 사용자가 비밀번호를 틀릴 시 예외가 발생한다.")
    @Test
    void occurWrongPasswordException() {
        //given
        String loginId = "loginId";
        String nickname = "nickname";
        String password = "password";
        String salt = "1234";
        accountRepository.save(Account.signUp(nickname, loginId, password, salt));
        LocalDateTime now = LocalDateTime.now();
        String wrongPassword = "1234";

        //when
        TodoListException todoListException = assertThrows(TodoListException.class,
            () -> accountWriteService.login(loginId, wrongPassword, now));

        //then
        assertThat(todoListException.getExceptionStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(todoListException.getExceptionMessage()).isEqualTo("아이디 또는 비밀번호가 올바르지 않습니다.");
    }

    @DisplayName("탈퇴하려는 회원이 존재하지 않을 경우 예외가 발생한다.")
    @Test
    void occurNotExistAccountWithdrawException() {
        //given
        Long accountId = -1L;

        //when
        TodoListException todoListException = assertThrows(TodoListException.class,
            () -> accountWriteService.withdraw(accountId));

        //then
        assertThat(todoListException.getExceptionStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(todoListException.getExceptionMessage()).isEqualTo("회원이 존재하지 않습니다.");
    }

    @DisplayName("회원 id로 회원탈퇴를 한다.")
    @Test
    void withdraw() {
        //given
        String loginId = "loginId";
        String nickname = "nickname";
        String password = "password";
        String salt = "1234";
        Account savedAccount = accountRepository.save(Account.signUp(nickname, loginId, password, salt));
        Long savedAccountId = savedAccount.getId();

        //when
        accountWriteService.withdraw(savedAccountId);

        //then
        Account account = accountRepository.findById(savedAccountId).orElse(null);
        assertThat(account).isNull();

    }

    @DisplayName("액세스 토큰의 만료시간이 5분 지나면 리프레쉬 토큰으로 재발급한다.")
    @Test
    void expiredAccessTokenUpdateRefreshToken() {
        //given
        String email = "test@test.com";
        String name = "name";
        String password = "password";
        String salt = "1234";
        Account savedAccount = accountRepository.save(Account.signUp(email, name, password, salt));
        String accessToken = "access";
        String refreshToken = "refresh";
        LocalDateTime accessTokenExpiredAt = LocalDateTime.of(2024, 5, 30, 10, 0, 0);
        LocalDateTime refreshTokenExpiredAt = LocalDateTime.of(2024, 5, 30, 11, 0, 0);
        Token token = Token.create(accessToken, accessTokenExpiredAt, savedAccount, refreshToken,
            refreshTokenExpiredAt);
        tokenRepository.save(token);

        LocalDateTime now = LocalDateTime.of(2024, 5, 30, 10, 6, 0);
        //when
        TokenResponse tokenResponse = accountWriteService.refreshToken(refreshToken, now);

        //then
        assertThat(tokenResponse.accessToken()).isNotEqualTo(accessToken);
        assertThat(tokenResponse.refreshToken()).isEqualTo(refreshToken);
        assertThat(tokenResponse.accessTokenExpireAt()).isEqualTo(now.plusMinutes(5));
    }

    @DisplayName("재발급 하려는 리프레쉬 토큰이 없으면 예외가 발생한다.")
    @Test
    void occurNotExistRefreshTokenException() {
        //given
        String email = "test@test.com";
        String name = "name";
        String password = "password";
        String salt = "salt";
        Account savedAccount = accountRepository.save(Account.signUp(email, name, password, salt));
        String accessToken = "access";
        String refreshToken = "refresh";
        LocalDateTime accessTokenExpiredAt = LocalDateTime.of(2024, 5, 30, 10, 0, 0);
        LocalDateTime refreshTokenExpiredAt = LocalDateTime.of(2024, 5, 30, 11, 0, 0);
        Token token = Token.create(accessToken, accessTokenExpiredAt, savedAccount, refreshToken,
            refreshTokenExpiredAt);
        tokenRepository.save(token);
        String requestRefreshToken = "not";

        //when
        TodoListException todoListException = assertThrows(TodoListException.class,
            () -> accountWriteService.refreshToken(requestRefreshToken, LocalDateTime.now()));

        //then
        assertThat(todoListException.getExceptionMessage()).isEqualTo("존재하지 않는 토큰 정보입니다.");
        assertThat(todoListException.getExceptionStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
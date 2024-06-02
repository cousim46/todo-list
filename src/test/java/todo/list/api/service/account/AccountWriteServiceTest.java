package todo.list.api.service.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import todo.list.domain.account.Account;
import todo.list.domain.account.AccountRepository;
import todo.list.exception.TodoListException;

@SpringBootTest
class AccountWriteServiceTest {

    @Autowired
    private AccountWriteService accountWriteService;

    @Autowired
    private AccountRepository accountRepository;

    @AfterEach
    void clear() {
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
        accountWriteService.singUp(loginId, nickname, password, salt);

        //then
        List<Account> findAllAccounts = accountRepository.findAll();
        assertThat(findAllAccounts)
            .hasSize(1)
            .extracting("nickname", "loginId", "salt")
            .contains(
                tuple(nickname, loginId, salt)
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
            () -> accountWriteService.singUp(loginId, inputNickname, password, salt));

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
            () -> accountWriteService.singUp(inputLoginId, nickname, password, salt));

        //then
        assertThat(todoListException.getExceptionMessage()).isEqualTo("중복된 닉네임이 존재합니다.");
        assertThat(todoListException.getExceptionStatus()).isEqualTo(HttpStatus.CONFLICT);
    }
}
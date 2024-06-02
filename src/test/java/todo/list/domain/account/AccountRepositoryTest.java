package todo.list.domain.account;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import todo.list.config.AuditingConfig;

@DataJpaTest
@Import(AuditingConfig.class)
class AccountRepositoryTest {
    @Autowired
    private AccountRepository accountRepository;

    @DisplayName("회원이 입력한 정보로 회원 데이터를 저장한다.")
    @Test
    void save() {
        //given
        String nickname = "nickname";
        String loginId = "loginId";
        String password = "password";
        String salt = "salt";

        Account account = Account.signUp(nickname, loginId, password, salt);

        //when
        Account savedAccount = accountRepository.save(account);

        //then
        assertThat(savedAccount.getNickname()).isEqualTo(nickname);
        assertThat(savedAccount.getLoginId()).isEqualTo(loginId);
        assertThat(savedAccount.getPassword()).isEqualTo(password);
        assertThat(savedAccount.getSalt()).isEqualTo(salt);
    }

    @DisplayName("아이디가 존재하지 않으면 false 반환한다.")
    @Test
    void notExistLoginIdReturnFalse() {
        //given
        String nickname = "nickname";
        String loginId = "loginId";
        String password = "password";
        String salt = "salt";
        Account account = Account.signUp(nickname, loginId, password, salt);
        accountRepository.save(account);
        String inputLoginId = "loginId2";
        //when
        boolean result = accountRepository.existsByLoginId(inputLoginId);

        //then
        assertThat(result).isFalse();
    }

    @DisplayName("아이디가 존재하면 true 반환한다.")
    @Test
    void existLoginIdReturnTrue() {
        //given
        String nickname = "nickname";
        String loginId = "loginId";
        String password = "password";
        String salt = "salt";
        Account account = Account.signUp(nickname, loginId, password, salt);
        accountRepository.save(account);

        //when
        boolean result = accountRepository.existsByLoginId(loginId);

        //then
        assertThat(result).isTrue();

    }


    @DisplayName("닉네임이 존재하지 않으면 false 반환한다.")
    @Test
    void notExistNicknameReturnFalse() {
        //given
        String nickname = "nickname";
        String loginId = "loginId";
        String password = "password";
        String salt = "salt";
        Account account = Account.signUp(nickname, loginId, password, salt);
        accountRepository.save(account);
        String inputNickname = "nickname2";

        //when
        boolean result = accountRepository.existsByNickname(inputNickname);

        //then
        assertThat(result).isFalse();
    }

    @DisplayName("닉네임이 존재하면 true 반환한다.")
    @Test
    void existNicknameReturnTrue() {
        //given
        String nickname = "nickname";
        String loginId = "loginId";
        String password = "password";
        String salt = "salt";
        Account account = Account.signUp(nickname, loginId, password, salt);
        accountRepository.save(account);

        //when
        boolean result = accountRepository.existsByNickname(nickname);

        //then
        assertThat(result).isTrue();
    }


}
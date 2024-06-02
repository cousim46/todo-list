package todo.list.domain.account;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
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

    @AfterEach
    void clear() {
        accountRepository.deleteAllInBatch();
    }

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
}
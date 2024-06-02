package todo.list.domain.account;

import static org.assertj.core.api.Assertions.assertThat;
import static todo.list.domain.account.enums.Role.USER;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AccountTest {

    @DisplayName("아이디, 비밀번호, 닉네임, salt로 회원을 생성한다. ")
    @Test
    void signUp() {
        //given
        String nickname = "nickname";
        String loginId = "loginId";
        String password = "password";
        String salt = "salt";

        //when
        Account account = Account.signUp(nickname, loginId, password, salt);

        //then
        assertThat(account.getNickname()).isEqualTo(nickname);
        assertThat(account.getLoginId()).isEqualTo(loginId);
        assertThat(account.getPassword()).isEqualTo(password);
        assertThat(account.getSalt()).isEqualTo(salt);
    }

    @DisplayName("회원이 생성되면 기본으로 '회원'권한을 갖는다.")
    @Test
    void saveAccountBaseRoleUser() {
        //given
        String nickname = "nickname";
        String loginId = "loginId";
        String password = "password";
        String salt = "salt";

        //when
        Account account = Account.signUp(nickname, loginId, password, salt);

        //then
        assertThat(account.getRole()).isEqualTo(USER);
    }

}
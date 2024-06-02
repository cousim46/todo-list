package todo.list.api.controller.account.dto.request;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AccountSignUpRequestTest {

    private static Validator validator;

    @BeforeAll
    public static void init() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @DisplayName("아이디, 닉네임, 비밀번호 값이 있을 경우 회원가입 요청 객체를 생성한다.")
    @Test
    void create() {
        //given
        String loginId = "loginId";
        String nickname = "nickname";
        String password = "password";

        //when
        AccountSignUpRequest accountSignUpRequest = new AccountSignUpRequest(loginId, nickname, password);

        //then
        assertThat(accountSignUpRequest.loginId()).isEqualTo(loginId);
        assertThat(accountSignUpRequest.password()).isEqualTo(password);
        assertThat(accountSignUpRequest.nickname()).isEqualTo(nickname);

    }
    @DisplayName("아이디 값이 null이면 예외가 발생한다.")
    @Test
    void occurIsNullLoginIdException() {
        //given
        String loginId = null;
        String nickname = "nickname";
        String password = "password";
        AccountSignUpRequest accountSignUpRequest = new AccountSignUpRequest(loginId, nickname, password);

        //when
        Set<ConstraintViolation<AccountSignUpRequest>> result = validator.validate(
            accountSignUpRequest);

        //then
        List<ConstraintViolation<AccountSignUpRequest>> response = new ArrayList<>(
            result);
        assertThat(response).hasSize(1)
            .extracting("message")
            .contains("공백일 수 없습니다");
    }
    @DisplayName("아이디 값이 비어 있으면 예외가 발생한다.")
    @Test
    void occurIsBlankLoginIdException() {
        String loginId = "";
        String nickname = "nickname";
        String password = "password";
        AccountSignUpRequest accountSignUpRequest = new AccountSignUpRequest(loginId, nickname, password);

        //when
        Set<ConstraintViolation<AccountSignUpRequest>> result = validator.validate(
            accountSignUpRequest);

        //then
        List<ConstraintViolation<AccountSignUpRequest>> response = new ArrayList<>(
            result);
        assertThat(response).hasSize(1)
            .extracting("message")
            .contains("공백일 수 없습니다");
    }

    @DisplayName("닉네입 값이 null이면 예외가 발생한다.")
    @Test
    void occurIsNullNicknameException() {
        //given
        String loginId = "loginId";
        String nickname = null;
        String password = "password";
        AccountSignUpRequest accountSignUpRequest = new AccountSignUpRequest(loginId, nickname, password);

        //when
        Set<ConstraintViolation<AccountSignUpRequest>> result = validator.validate(
            accountSignUpRequest);

        //then
        List<ConstraintViolation<AccountSignUpRequest>> response = new ArrayList<>(
            result);
        assertThat(response).hasSize(1)
            .extracting("message")
            .contains("공백일 수 없습니다");
    }
    @DisplayName("닉네임 값이 비어 있으면 예외가 발생한다.")
    @Test
    void occurIsBlankNicknameException() {
        String loginId = "loginId";
        String nickname = "";
        String password = "password";
        AccountSignUpRequest accountSignUpRequest = new AccountSignUpRequest(loginId, nickname, password);

        //when
        Set<ConstraintViolation<AccountSignUpRequest>> result = validator.validate(
            accountSignUpRequest);

        //then
        List<ConstraintViolation<AccountSignUpRequest>> response = new ArrayList<>(
            result);
        assertThat(response).hasSize(1)
            .extracting("message")
            .contains("공백일 수 없습니다");
    }
    @DisplayName("비밀번호 값이 null이면 예외가 발생한다.")
    @Test
    void occurIsNullPasswordException() {
        //given
        String loginId = "loginId";
        String nickname = "nickname";
        String password = null;
        AccountSignUpRequest accountSignUpRequest = new AccountSignUpRequest(loginId, nickname, password);

        //when
        Set<ConstraintViolation<AccountSignUpRequest>> result = validator.validate(
            accountSignUpRequest);

        //then
        List<ConstraintViolation<AccountSignUpRequest>> response = new ArrayList<>(
            result);
        assertThat(response).hasSize(1)
            .extracting("message")
            .contains("공백일 수 없습니다");
    }
    @DisplayName("비밀번호 값이 비어 있으면 예외가 발생한다.")
    @Test
    void occurIsBlankPasswordException() {
        String loginId = "loginId";
        String nickname = "nickname";
        String password = "";
        AccountSignUpRequest accountSignUpRequest = new AccountSignUpRequest(loginId, nickname, password);

        //when
        Set<ConstraintViolation<AccountSignUpRequest>> result = validator.validate(
            accountSignUpRequest);

        //then
        List<ConstraintViolation<AccountSignUpRequest>> response = new ArrayList<>(
            result);
        assertThat(response).hasSize(1)
            .extracting("message")
            .contains("공백일 수 없습니다");
    }
}
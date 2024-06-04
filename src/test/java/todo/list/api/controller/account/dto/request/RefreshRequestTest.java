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

class RefreshRequestTest {
    private static Validator validator;

    @BeforeAll
    public static void init() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @DisplayName("리프레쉬 토큰 값으로 객체를 생성한다.")
    @Test
    void create() {
        //given
        String refreshToken = "refreshToken";

        //when
        RefreshRequest refreshRequest = new RefreshRequest(refreshToken);

        //then
        assertThat(refreshRequest.token()).isEqualTo(refreshToken);
    }

    @DisplayName("제목이 공백이면 예외가 발생한다.")
    @Test
    void occurIsBlankTitleException() {
        //given
        String refreshToken = "";
        RefreshRequest refreshRequest = new RefreshRequest(refreshToken);

        //when
        Set<ConstraintViolation<RefreshRequest>> result = validator.validate(
            refreshRequest);

        //then
        List<ConstraintViolation<RefreshRequest>> response = new ArrayList<>(
            result);
        assertThat(response)
            .hasSize(1)
            .extracting("message")
            .contains("공백일 수 없습니다");
    }

    @DisplayName("제목이 null이면 예외가 발생한다.")
    @Test
    void occurIsNullTitleException() {
        //given
        String refreshToken = null;
        RefreshRequest refreshRequest = new RefreshRequest(refreshToken);

        //when
        Set<ConstraintViolation<RefreshRequest>> result = validator.validate(
            refreshRequest);

        //then
        List<ConstraintViolation<RefreshRequest>> response = new ArrayList<>(
            result);
        assertThat(response)
            .hasSize(1)
            .extracting("message")
            .contains("공백일 수 없습니다");
    }
}
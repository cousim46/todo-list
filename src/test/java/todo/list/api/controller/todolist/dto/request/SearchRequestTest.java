package todo.list.api.controller.todolist.dto.request;

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
import todo.list.api.service.todolist.dto.enums.SearchType;

class SearchRequestTest {
    private static Validator validator;

    @BeforeAll
    public static void init() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @DisplayName("조회하려는 타입이 없으면 예외가 발생한다.")
    @Test
    void occurIsNullTypeException() {
        //given
        SearchType type = null;
        SearchRequest searchRequest = new SearchRequest(type);

        //when
        Set<ConstraintViolation<SearchRequest>> result = validator.validate(searchRequest);

        //then
        List<ConstraintViolation<SearchRequest>> response = new ArrayList<>(
            result);
        assertThat(response)
            .hasSize(1)
            .extracting("message")
            .contains("널이어서는 안됩니다");
    }
}
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

class TodoCreateRequestTest {
    private static Validator validator;

    @BeforeAll
    public static void init() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }
    @DisplayName("제목과 내용으로 Todo 객체를 생성한다.")
    @Test
    void create() {
        //given
        String title = "title";
        String content = "content";

        //when
        TodoCreateRequest todoCreateRequest = new TodoCreateRequest(title, content);

        //then
        assertThat(todoCreateRequest.content()).isEqualTo(content);
        assertThat(todoCreateRequest.title()).isEqualTo(title);
    }

    @DisplayName("제목이 null이면 예외가 발생한다.")
    @Test
    void occurIsNullTitleException() {
        //given
        String title = null;
        String content = "content";
        TodoCreateRequest todoCreateRequest = new TodoCreateRequest(title, content);

        //when
        Set<ConstraintViolation<TodoCreateRequest>> result = validator.validate(
            todoCreateRequest);

        //then
        List<ConstraintViolation<TodoCreateRequest>> response = new ArrayList<>(
            result);
        assertThat(response)
            .hasSize(1)
            .extracting("message")
            .contains("공백일 수 없습니다");

    }
}
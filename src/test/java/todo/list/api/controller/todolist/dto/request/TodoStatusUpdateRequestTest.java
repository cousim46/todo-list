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
import todo.list.domain.todolist.enums.TodoStatus;

class TodoStatusUpdateRequestTest {
    private static Validator validator;

    @BeforeAll
    public static void init() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @DisplayName("변경 할 상태값이 null이면 예외가 발생한다.")
    @Test
    void occurIsNullStatusException() {
        //given
        TodoStatusUpdateRequest todoStatusUpdateRequest = new TodoStatusUpdateRequest(null);

        //when
        Set<ConstraintViolation<TodoStatusUpdateRequest>> result = validator.validate(todoStatusUpdateRequest);

        //then
        List<ConstraintViolation<TodoStatusUpdateRequest>> response = new ArrayList<>(
            result);
        assertThat(response)
            .hasSize(1)
            .extracting("message")
            .contains("널이어서는 안됩니다");
    }

    @DisplayName("변경 할 상태값이 null이 아니면 객체를 생성한다.")
    @Test
    void create() {
        //given
        TodoStatus status = TodoStatus.TODO;

        //when
        TodoStatusUpdateRequest todoStatusUpdateRequest = new TodoStatusUpdateRequest(status);

        //then
        assertThat(todoStatusUpdateRequest.status()).isEqualTo(status);
    }
}
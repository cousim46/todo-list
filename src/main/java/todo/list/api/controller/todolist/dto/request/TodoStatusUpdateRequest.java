package todo.list.api.controller.todolist.dto.request;

import jakarta.validation.constraints.NotNull;
import todo.list.domain.todolist.enums.TodoStatus;

public record TodoStatusUpdateRequest(
    @NotNull
    TodoStatus status
) {
}

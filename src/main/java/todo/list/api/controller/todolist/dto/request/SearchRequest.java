package todo.list.api.controller.todolist.dto.request;

import jakarta.validation.constraints.NotNull;
import todo.list.api.service.todolist.dto.enums.SearchType;

public record SearchRequest(
    @NotNull
    SearchType type
) {
}

package todo.list.api.controller.todolist.dto.request;


import jakarta.validation.constraints.NotBlank;

public record TodoCreateRequest(
    @NotBlank
    String title,
    String content
) {

}

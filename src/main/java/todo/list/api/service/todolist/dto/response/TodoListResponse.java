package todo.list.api.service.todolist.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import todo.list.domain.todolist.Todo;
import todo.list.domain.todolist.enums.TodoStatus;

public record TodoListResponse(
    String nickname,
    String title,
    String content,
    TodoStatus status,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdAt,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime updatedAt
) {
    public static TodoListResponse of(Todo todo, String nickname) {
        return new TodoListResponse(
            nickname,
            todo.getTitle(),
            todo.getContent(),
            todo.getStatus(),
            todo.getCreatedAt(),
            todo.getUpdatedAt()
        );
    }
}

package todo.list.domain.todolist.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TodoStatus {
    TODO("할 일"), IN_PROGRESS("진행중"),DONE("완료"), PENDING("대기");

    private final String content;

}

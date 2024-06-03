package todo.list.api.service.todolist.dto.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SearchType {
    RECENT("최근"), ALL("전체");

    private final String content;
}

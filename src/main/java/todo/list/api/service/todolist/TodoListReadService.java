package todo.list.api.service.todolist;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import todo.list.api.service.annotaion.ReadService;
import todo.list.api.service.todolist.dto.enums.SearchType;
import todo.list.api.service.todolist.dto.response.TodoListResponse;
import todo.list.domain.account.Account;
import todo.list.domain.account.AccountRepository;
import todo.list.domain.todolist.Todo;
import todo.list.domain.todolist.TodoRepository;
import todo.list.exception.ErrorCode;
import todo.list.exception.TodoListException;

@RequiredArgsConstructor
@ReadService
public class TodoListReadService {

    private final TodoRepository todoRepository;
    private final AccountRepository accountRepository;


    public List<TodoListResponse> findAllOrRecent(SearchType type, Long accountId) {
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new TodoListException(ErrorCode.NOT_EXIST_ACCOUNT_INFO));
        List<Todo> todoList = findAllTodo(type, accountId);

        return todoList.stream().map(todo -> TodoListResponse.of(todo, account.getNickname()))
            .collect(
                Collectors.toList());
    }

    private List<Todo> findAllTodo(SearchType type, Long accountId) {
        if (type == SearchType.RECENT) {
            Todo todoRecent = todoRepository.findRecent(accountId).orElse(null);
            return todoRecent != null ? List.of(todoRecent) : List.of();
        }
        return todoRepository.findAllByAccountIdOrderByUpdatedAtDesc(accountId);
    }
}

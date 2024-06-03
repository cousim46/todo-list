package todo.list.api.service.todolist;

import lombok.RequiredArgsConstructor;
import todo.list.api.service.annotaion.WriteService;
import todo.list.domain.account.Account;
import todo.list.domain.account.AccountRepository;
import todo.list.domain.todolist.Todo;
import todo.list.domain.todolist.TodoRepository;
import todo.list.exception.ErrorCode;
import todo.list.exception.TodoListException;

@RequiredArgsConstructor
@WriteService
public class TodoListWriteService {

    private final AccountRepository accountRepository;
    private final TodoRepository todoRepository;

    public void create(Long accountId, String title, String content) {
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new TodoListException(ErrorCode.NOT_EXIST_ACCOUNT_INFO));
        todoRepository.save(Todo.create(title,content,account));
    }
}

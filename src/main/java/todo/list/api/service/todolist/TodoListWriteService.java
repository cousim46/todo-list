package todo.list.api.service.todolist;

import static todo.list.exception.ErrorCode.NOT_CHANGE_STATUS;
import static todo.list.exception.ErrorCode.NOT_EXIST_TODO;

import lombok.RequiredArgsConstructor;
import todo.list.api.service.annotaion.WriteService;
import todo.list.domain.account.Account;
import todo.list.domain.account.AccountRepository;
import todo.list.domain.todolist.Todo;
import todo.list.domain.todolist.TodoRepository;
import todo.list.domain.todolist.enums.TodoStatus;
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
        todoRepository.save(Todo.create(title, content, account));
    }

    public void updateStatus(Long accountId, Long todoId, TodoStatus todoStatus) {
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new TodoListException(ErrorCode.NOT_EXIST_ACCOUNT_INFO));
        Todo todo = todoRepository.findByIdAndAccount(todoId, account)
            .orElseThrow(() -> new TodoListException(NOT_EXIST_TODO));
        checkStatus(todoStatus, todo);
        todo.updateStatus(todoStatus);
    }

    private static void checkStatus(TodoStatus requestTodoStatus, Todo todo) {
        if(requestTodoStatus == TodoStatus.PENDING) {
            if(todo.getStatus() != TodoStatus.IN_PROGRESS) {
                throw new TodoListException(NOT_CHANGE_STATUS);
            }
        }
    }
}

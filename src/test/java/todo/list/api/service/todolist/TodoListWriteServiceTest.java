package todo.list.api.service.todolist;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static todo.list.domain.todolist.enums.TodoStatus.IN_PROGRESS;
import static todo.list.domain.todolist.enums.TodoStatus.PENDING;

import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import todo.list.domain.account.Account;
import todo.list.domain.account.AccountRepository;
import todo.list.domain.todolist.Todo;
import todo.list.domain.todolist.TodoRepository;
import todo.list.domain.todolist.enums.TodoStatus;
import todo.list.exception.TodoListException;

@SpringBootTest
class TodoListWriteServiceTest {

    @Autowired
    private TodoListWriteService todoListWriteService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TodoRepository todoRepository;

    @AfterEach
    void clear() {
        todoRepository.deleteAllInBatch();
        accountRepository.deleteAllInBatch();
    }

    @DisplayName("회원은 제목과 내용으로 Todo를 생성한다.")
    @Test
    void create() {
        //given
        String nickname = "nickname";
        String loginId = "loginId";
        String password = "password";
        String salt = "salt";
        Account savedAccount = accountRepository.save(
            Account.signUp(nickname, loginId, password, salt));

        String title = "title";
        String content = "content";

        //when
        todoListWriteService.create(savedAccount.getId(), title, content);

        //then
        List<Todo> todoList = todoRepository.findAll();
        assertThat(todoList)
            .hasSize(1)
            .extracting("title", "content", "status")
            .contains(
                tuple(title, content, TodoStatus.TODO)
            );
    }

    @DisplayName("Todo를 생성하면 '할 일' 상태를 기본으로 갖는다.")
    @Test
    void createTodoBaseStatusTODO() {
        //given
        String nickname = "nickname";
        String loginId = "loginId";
        String password = "password";
        String salt = "salt";
        Account savedAccount = accountRepository.save(
            Account.signUp(nickname, loginId, password, salt));

        String title = "title";
        String content = "content";

        //when
        todoListWriteService.create(savedAccount.getId(), title, content);

        //then
        List<Todo> todoList = todoRepository.findAll();
        assertThat(todoList)
            .hasSize(1)
            .extracting("status")
            .contains(
                TodoStatus.TODO
            );
    }

    @DisplayName("Todo를 생성하려는 회원이 존재하지 않으면 예외가 발생한다.")
    @Test
    void occurCreateTodoNotExistAccountException() {
        //given
        String title = "title";
        String content = "content";
        Long accountId = -1L;

        //when
        TodoListException todoListException = assertThrows(TodoListException.class,
            () -> todoListWriteService.create(accountId, title, content));

        //then
        assertThat(todoListException.getExceptionMessage()).isEqualTo("회원이 존재하지 않습니다.");
        assertThat(todoListException.getExceptionStatus()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    @DisplayName("Todo의 상태값을 변경 할 수 있다.")
    @Test
    void updateStatus() {
        //given
        String nickname = "nickname";
        String loginId = "loginId";
        String password = "password";
        String salt = "salt";
        Account savedAccount = accountRepository.save(
            Account.signUp(nickname, loginId, password, salt));

        String title = "title";
        String content = "content";
        Todo savedTodo = todoRepository.save(Todo.create(title, content, savedAccount));

        TodoStatus beforeStatus = savedTodo.getStatus();

        //when
        todoListWriteService.updateStatus(savedAccount.getId(), savedTodo.getId(), IN_PROGRESS);

        //then
        Todo todo = todoRepository.findById(savedTodo.getId()).get();
        assertThat(todo.getStatus()).isNotEqualTo(beforeStatus);
        assertThat(todo.getStatus()).isEqualTo(IN_PROGRESS);
    }
    @DisplayName("Todo의 상태값을 PENDING으로 변경할 때 IN_PROGRESS 상태가 아닐 경우에 예외가 발생한다.")
    @Test
    void occurNotStatusInProgressChangePendingException() {
        //given
        String nickname = "nickname";
        String loginId = "loginId";
        String password = "password";
        String salt = "salt";
        Account savedAccount = accountRepository.save(
            Account.signUp(nickname, loginId, password, salt));

        String title = "title";
        String content = "content";
        Todo savedTodo = todoRepository.save(Todo.create(title, content, savedAccount));

        //when
        TodoListException todoListException = assertThrows(TodoListException.class,
            () -> todoListWriteService.updateStatus(savedAccount.getId(), savedTodo.getId(),
                PENDING));

        //then
        assertThat(todoListException.getExceptionMessage()).isEqualTo("대기 상태는 진행 상태일때만 상태 변경이 가능합니다.");
        assertThat(todoListException.getExceptionStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
    @DisplayName("상태 변경하려는 사용자가 존재하지 않으면 예외가 발생한다.")
    @Test
    void occurNotExistAccountException() {
        //given
        String nickname = "nickname";
        String loginId = "loginId";
        String password = "password";
        String salt = "salt";
        Account savedAccount = accountRepository.save(
            Account.signUp(nickname, loginId, password, salt));
        String title = "title";
        String content = "content";
        Todo savedTodo = todoRepository.save(Todo.create(title, content, savedAccount));
        Long requestAccountId = -1L;

        //when
        TodoListException todoListException = assertThrows(TodoListException.class,
            () -> todoListWriteService.updateStatus(requestAccountId, savedTodo.getId(),
                IN_PROGRESS));

        //then
        assertThat(todoListException.getExceptionMessage()).isEqualTo("회원이 존재하지 않습니다.");
        assertThat(todoListException.getExceptionStatus()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @DisplayName("상태 변경하려는 회원이 회원의 것이 아닌 Todo를 변경하려고 할때 예외가 발생한다.")
    @Test
    void occurNotExistTodoException() {
        String nickname = "nickname";
        String loginId = "loginId";
        String password = "password";
        String salt = "salt";
        Account savedAccount = accountRepository.save(
            Account.signUp(nickname, loginId, password, salt));
        Long requestTodoId = -1L;


        //when
        TodoListException todoListException = assertThrows(TodoListException.class,
            () -> todoListWriteService.updateStatus(savedAccount.getId(), requestTodoId,
                IN_PROGRESS));

        //then
        assertThat(todoListException.getExceptionMessage()).isEqualTo("존재하지 않는 Todo입니다.");
        assertThat(todoListException.getExceptionStatus()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
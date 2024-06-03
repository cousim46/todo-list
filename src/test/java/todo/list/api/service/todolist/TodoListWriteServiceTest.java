package todo.list.api.service.todolist;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
}
package todo.list.api.service.todolist;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import todo.list.api.service.todolist.dto.enums.SearchType;
import todo.list.api.service.todolist.dto.response.TodoListResponse;
import todo.list.domain.account.Account;
import todo.list.domain.account.AccountRepository;
import todo.list.domain.todolist.Todo;
import todo.list.domain.todolist.TodoRepository;
import todo.list.exception.TodoListException;

@SpringBootTest
class TodoListReadServiceTest {
    @Autowired
    private TodoListReadService todoListReadService;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TodoRepository todoRepository;

    @AfterEach
    void clear() {
        todoRepository.deleteAllInBatch();
        accountRepository.deleteAllInBatch();
    }

    @DisplayName("검색 타입이 ALL일 경우 로그인 한 사용자가 작성한 Todo를 업데이트 시간 기준으로 내림차순으로 조회한다.")
    @Test
    void findAllSearchTypeAllUpdateAtDesc() {
        //given
        String loginId = "loginId";
        String nickname = "nickname";
        String password = "password";
        String salt = "1234";
        Account savedAccount = accountRepository.save(Account.signUp(nickname, loginId, password, salt));

        Todo todo1 = Todo.create("title1", "content1", savedAccount);
        Todo todo2 = Todo.create("title2", "content2", savedAccount);
        Todo todo3 = Todo.create("title3", "content3", savedAccount);
        todoRepository.saveAllAndFlush(List.of(todo1,todo2,todo3));

        //when
        List<TodoListResponse> todoList = todoListReadService.findAllOrRecent(SearchType.ALL,savedAccount.getId());

        //then
        assertThat(todoList)
            .hasSize(3)
            .extracting("title", "content", "nickname")
            .contains(
                tuple("title1", "content1", nickname),
                tuple("title2", "content2", nickname),
                tuple("title3", "content3", nickname)
            );
        assertThat(todoList.get(0).updatedAt().isAfter(todoList.get(1).updatedAt())).isTrue();
        assertThat(todoList.get(0).updatedAt().isAfter(todoList.get(2).updatedAt())).isTrue();
    }

    @DisplayName("검색 타입이 RECENT 경우 로그인 한 사용자가 최신에 등록한 Todo를 조회한다.")
    @Test
    void findSearchTypeRecentTodo() {
        //given
        String loginId = "loginId";
        String nickname = "nickname";
        String password = "password";
        String salt = "1234";
        Account savedAccount = accountRepository.save(Account.signUp(nickname, loginId, password, salt));

        Todo todo1 = Todo.create("title1", "content1", savedAccount);
        Todo todo2 = Todo.create("title2", "content2", savedAccount);
        Todo todo3 = Todo.create("title3", "content3", savedAccount);
        todoRepository.saveAllAndFlush(List.of(todo1,todo2,todo3));

        //when
        List<TodoListResponse> todoList = todoListReadService.findAllOrRecent(SearchType.RECENT,savedAccount.getId());

        //then
        assertThat(todoList)
            .hasSize(1)
            .extracting("title", "content", "nickname")
            .contains(Tuple.tuple("title3", "content3", nickname));
    }

    @DisplayName("조회하려는 회원이 존재하지 않을 경우 예외가 발생한다. ")
    @Test
    void occurNotExistAccountException() {
        //given
        Long accountId = -1L;

        //when
        TodoListException todoListException = assertThrows(TodoListException.class,
            () -> todoListReadService.findAllOrRecent(SearchType.RECENT, accountId));

        //then
        assertThat(todoListException.getExceptionMessage()).isEqualTo("회원이 존재하지 않습니다.");
        assertThat(todoListException.getExceptionStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
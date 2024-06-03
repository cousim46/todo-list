package todo.list.domain.todolist;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import todo.list.domain.account.Account;
import todo.list.domain.todolist.enums.TodoStatus;

class TodoTest {

    @DisplayName("회원은 제목, 내용으로 Todo 생성한다.")
    @Test
    void create() {
        //given
        String nickname = "nickname";
        String loginId = "loginId";
        String password = "password";
        String salt = "salt";
        Account account = Account.signUp(nickname, loginId, password, salt);

        String title = "title";
        String content = "content";

        //when
        Todo todo = Todo.create(title, content, account);

        //then
        assertThat(todo.getContent()).isEqualTo(content);
        assertThat(todo.getTitle()).isEqualTo(title);
        assertThat(todo.getAccount()).isEqualTo(account);
    }

    @DisplayName("Todo가 생성되면 기본으로 '할 일' 상태값을 갖는다.")
    @Test
    void createTodoListBaseStatusTODO() {
        //given
        String nickname = "nickname";
        String loginId = "loginId";
        String password = "password";
        String salt = "salt";
        Account account = Account.signUp(nickname, loginId, password, salt);

        String title = "title";
        String content = "content";

        //when
        Todo todo = Todo.create(title, content, account);

        //then
        assertThat(todo.getStatus()).isEqualTo(TodoStatus.TODO);
    }
    @DisplayName("Todo의 상태를 변경한다.")
    @Test
    void updateStatus() {
        String nickname = "nickname";
        String loginId = "loginId";
        String password = "password";
        String salt = "salt";
        Account account = Account.signUp(nickname, loginId, password, salt);

        String title = "title";
        String content = "content";

        Todo todo = Todo.create(title, content, account);
        TodoStatus beforeStatus = todo.getStatus();
        TodoStatus afterStatus = TodoStatus.IN_PROGRESS;

        //when
        todo.updateStatus(afterStatus);

        //then
        assertThat(todo.getStatus()).isEqualTo(afterStatus);
        assertThat(todo.getStatus()).isNotEqualTo(beforeStatus);
    }

}
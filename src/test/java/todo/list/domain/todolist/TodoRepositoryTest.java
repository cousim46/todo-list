package todo.list.domain.todolist;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import todo.list.config.AuditingConfig;
import todo.list.domain.account.Account;
import todo.list.domain.account.AccountRepository;

@DataJpaTest
@Import(AuditingConfig.class)
class TodoRepositoryTest {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TodoRepository todoRepository;

    @DisplayName("유저가 최근에 등록한 Todo 1건을 조회한다.")
    @Test
    void findRecent() {
        //given
        String nickname = "nickname";
        String loginId = "loginId";
        String password = "password";
        String salt = "salt";
        Account account = Account.signUp(nickname, loginId, password, salt);
        Account savedAccount = accountRepository.save(account);

        Todo todo1 = Todo.create("title1", "content1", savedAccount);
        Todo todo2 = Todo.create("title2", "content2", savedAccount);
        Todo todo3 = Todo.create("title3", "content3", savedAccount);
        todoRepository.saveAllAndFlush(List.of(todo1,todo2,todo3));

        //when
        Optional<Todo> recentTodo = todoRepository.findRecent(savedAccount.getId());

        //then
        Todo todo = recentTodo.get();
        assertThat(todo).isNotNull();
        assertThat(todo.getContent()).isEqualTo(todo3.getContent());
        assertThat(todo.getTitle()).isEqualTo(todo3.getTitle());
    }

    @DisplayName("유저가 등록한 Todo를 수정 시간 기준 내림차순으로 모두 조회한다.")
    @Test
    void findAllByAccountId() {
        //given
        String nickname = "nickname";
        String loginId = "loginId";
        String password = "password";
        String salt = "salt";
        Account account = Account.signUp(nickname, loginId, password, salt);
        Account savedAccount = accountRepository.save(account);

        Todo todo1 = Todo.create("title1", "content1", savedAccount);
        Todo todo2 = Todo.create("title2", "content2", savedAccount);
        Todo todo3 = Todo.create("title3", "content3", savedAccount);
        todoRepository.saveAllAndFlush(List.of(todo1,todo2,todo3));

        //when
        List<Todo> recentTodo = todoRepository.findAllByAccountIdOrderByUpdatedAtDesc(savedAccount.getId());

        //then
        Assertions.assertThat(recentTodo)
            .hasSize(3)
            .extracting("title", "content")
            .contains(
                tuple("title3", "content3"),
                tuple("title2", "content2"),
                tuple("title1", "content1")
            );
    }

    @DisplayName("Todo id와 회원으로 Todo를 조회한다.")
    @Test
    void findByIdAndAccount() {
        String nickname = "nickname";
        String loginId = "loginId";
        String password = "password";
        String salt = "salt";
        Account account = Account.signUp(nickname, loginId, password, salt);
        Account savedAccount = accountRepository.save(account);
        String title = "title";
        String content = "content";
        Todo todo = Todo.create(title, content, savedAccount);
        Todo savedTodo = todoRepository.save(todo);

        //when
        Optional<Todo> findTodo = todoRepository.findByIdAndAccount(savedTodo.getId(),
            savedAccount);

        //then
        Todo getTodo = findTodo.get();
        assertThat(getTodo.getContent()).isEqualTo(content);
        assertThat(getTodo.getTitle()).isEqualTo(title);
        assertThat(getTodo.getId()).isEqualTo(savedTodo.getId());
    }
}
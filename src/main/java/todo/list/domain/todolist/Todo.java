package todo.list.domain.todolist;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import todo.list.domain.BaseEntity;
import todo.list.domain.account.Account;
import todo.list.domain.todolist.enums.TodoStatus;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Todo extends BaseEntity {

    @Column(nullable = false)
    private String title;

    private String content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TodoStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;

    private Todo(String title, String content, TodoStatus status, Account account) {
        this.title = title;
        this.content = content;
        this.status = status;
        this.account = account;
    }

    public static Todo create(String title, String content, Account account) {
        return new Todo(title, content, TodoStatus.TODO, account);
    }
    public String returnStatusName() {
        return status.name();
    }
}

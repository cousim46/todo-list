package todo.list.domain.account;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import todo.list.domain.BaseEntity;
import todo.list.domain.account.enums.Role;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
    uniqueConstraints = {@UniqueConstraint(
        name = "unique_login_id", columnNames = "login_id"
    ), @UniqueConstraint(
        name = "unique_nickname", columnNames = "nickname"
    )
    }
)
public class Account extends BaseEntity {

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    private String salt;

    private Account(String nickname, String loginId, String password, Role role, String salt) {
        this.nickname = nickname;
        this.loginId = loginId;
        this.password = password;
        this.role = role;
        this.salt = salt;
    }

    public static Account signUp(String nickname, String loginId, String password, String salt) {
        return new Account(nickname, loginId, password, Role.USER, salt);
    }

    public String getRoleName() {
        return role.name();
    }
}

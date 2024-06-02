package todo.list.domain.token;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import todo.list.domain.BaseEntity;
import todo.list.domain.account.Account;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Token extends BaseEntity {

    @Column(nullable = false)
    private String access;

    @Column(nullable = false)
    private String refresh;

    @Column(nullable = false)
    private LocalDateTime accessExpireAt;

    @Column(nullable = false)
    private LocalDateTime refreshExpireAt;

    @OneToOne(fetch = FetchType.LAZY)
    private Account account;

    private Token(String access, LocalDateTime accessExpireAt, Account account, String refresh, LocalDateTime refreshExpireAt) {
        this.access = access;
        this.accessExpireAt = accessExpireAt;
        this.account = account;
        this.refresh = refresh;
        this.refreshExpireAt = refreshExpireAt;
    }

    public static Token create(String access, LocalDateTime accessExpireAt, Account account, String refresh,LocalDateTime refreshExpireAt) {
        return new Token(access, accessExpireAt, account, refresh, refreshExpireAt);
    }
    public void updateAccessToken(String access, LocalDateTime accessExpireAt) {
        this.access = access;
        this.accessExpireAt = accessExpireAt;
    }
    public void updateRefreshToken(String refresh, LocalDateTime refreshExpireAt) {
        this.refresh = refresh;
        this.refreshExpireAt = refreshExpireAt;
    }
}

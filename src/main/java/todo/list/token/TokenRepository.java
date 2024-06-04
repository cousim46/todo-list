package todo.list.token;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import todo.list.domain.token.Token;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByAccountId(Long accountId);

    @Query("SELECT t FROM Token t JOIN FETCH t.account WHERE t.access = :access")
    Optional<Token> findTokenLoginInfo(@Param("access") String access);

    @Query("SELECT t FROM Token t JOIN FETCH t.account WHERE t.refresh = :refresh")
    Optional<Token> findByRefreshToken(@Param("refresh") String refresh);
}

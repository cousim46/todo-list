package todo.list.domain.todolist;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import todo.list.domain.account.Account;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    @Query(value = "SELECT * FROM todo WHERE account_id = :accountId ORDER BY updated_at DESC LIMIT 1", nativeQuery = true)
    Optional<Todo> findRecent(@Param("accountId") Long accountId);

    List<Todo> findAllByAccountIdOrderByUpdatedAtDesc(@Param("accountId") Long accountId);
    Optional<Todo> findByIdAndAccount(Long id, Account account);
}

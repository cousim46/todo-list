package todo.list.api.service.account;


import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import todo.list.api.service.annotaion.WriteService;
import todo.list.domain.account.Account;
import todo.list.domain.account.AccountRepository;
import todo.list.exception.ErrorCode;
import todo.list.exception.TodoListException;

@RequiredArgsConstructor
@WriteService
public class AccountWriteService {
    private final BCryptPasswordEncoder encoder;
    private final AccountRepository accountRepository;

    public void singUp(String loginId, String nickname, String password, String salt) {
        duplicateValidation(loginId, nickname);
        String encodePassword = encoder.encode(password + salt);
        accountRepository.save(Account.signUp(nickname,loginId, encodePassword, salt));
    }

    private void duplicateValidation(String loginId, String nickname) {
        if(accountRepository.existsByLoginId(loginId)) {
            throw new TodoListException(ErrorCode.DUPLICATE_LOGIN_ID);
        }
        if(accountRepository.existsByNickname(nickname)) {
            throw new TodoListException(ErrorCode.DUPLICATE_NICKNAME);
        }
    }
}

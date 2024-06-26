package todo.list.api.service.account;


import static todo.list.exception.ErrorCode.NOT_EXIST_ACCOUNT_INFO;
import static todo.list.exception.ErrorCode.NOT_EXIST_TOKEN_INFO;
import static todo.list.exception.ErrorCode.NOT_MATCH_PASSWORD_OR_ID;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import todo.list.api.service.annotaion.WriteService;
import todo.list.domain.account.Account;
import todo.list.domain.account.AccountRepository;
import todo.list.domain.token.Token;
import todo.list.exception.ErrorCode;
import todo.list.exception.TodoListException;
import todo.list.token.TokenProvider;
import todo.list.token.TokenRepository;
import todo.list.token.response.TokenResponse;

@RequiredArgsConstructor
@WriteService
public class AccountWriteService {
    private final BCryptPasswordEncoder encoder;
    private final AccountRepository accountRepository;
    private final TokenProvider tokenProvider;
    private final TokenRepository tokenRepository;

    public void signUp(String loginId, String nickname, String password, String salt) {
        duplicateValidation(loginId, nickname);
        String encodeSalt = encoder.encode(salt);
        String encodePassword = encoder.encode(password + encodeSalt);
        accountRepository.save(Account.signUp(nickname, loginId, encodePassword, encodeSalt));
    }

    public TokenResponse login(String loginId, String password, LocalDateTime now) {
        Account account = accountRepository.findByLoginId(loginId).orElseThrow(() ->
            new TodoListException(NOT_MATCH_PASSWORD_OR_ID));
        checkPassword(password, account.getPassword(), account.getSalt());
        return tokenProvider.get(account.getId(), account.getRoleName(),now);
    }

    public void withdraw(Long accountId) {
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new TodoListException(NOT_EXIST_ACCOUNT_INFO));
        accountRepository.delete(account);
    }

    public TokenResponse refreshToken(String refreshToken, LocalDateTime now) {
        Token token = tokenRepository.findByRefreshToken(refreshToken)
            .orElseThrow(() -> new TodoListException(NOT_EXIST_TOKEN_INFO));
        return tokenProvider.get(token.returnAccountId(), token.returnRole(), now);
    }

    private void checkPassword(String inputPassword,String encodePassword, String salt) {
        if(!encoder.matches(inputPassword + salt, encodePassword)) {
            throw new TodoListException(NOT_MATCH_PASSWORD_OR_ID);
        }
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

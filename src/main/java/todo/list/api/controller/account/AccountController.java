package todo.list.api.controller.account;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import todo.list.api.controller.account.dto.request.AccountSignUpRequest;
import todo.list.api.controller.account.dto.request.LoginRequest;
import todo.list.api.controller.account.dto.response.TokenInfoResponse;
import todo.list.api.controller.account.helper.PasswordHelper;
import todo.list.api.controller.annotation.LoginUser;
import todo.list.api.controller.dto.request.LoginUserInfo;
import todo.list.api.dto.response.CommonResponse;
import todo.list.api.service.account.AccountWriteService;

@RequiredArgsConstructor
@RequestMapping("/api/accounts")
@RestController
public class AccountController {

    private final AccountWriteService accountWriteService;

    @PostMapping
    public void signUp(@Valid @RequestBody AccountSignUpRequest request) {
        accountWriteService.signUp(request.loginId(), request.nickname(), request.password(),
            PasswordHelper.createSalt());
    }

    @PostMapping("/login")
    public CommonResponse<TokenInfoResponse> login(@Valid @RequestBody LoginRequest request) {
        return CommonResponse.ok(TokenInfoResponse.of(
            accountWriteService.login(request.loginId(), request.password(), LocalDateTime.now())));
    }

    @DeleteMapping
    @Secured("ROLE_USER")
    public void withdraw(@LoginUser LoginUserInfo loginUserInfo) {
        accountWriteService.withdraw(loginUserInfo.id());
    }
}

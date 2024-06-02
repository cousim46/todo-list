package todo.list.api.controller.account;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import todo.list.api.controller.account.dto.request.AccountSignUpRequest;
import todo.list.api.controller.account.helper.PasswordHelper;
import todo.list.api.service.account.AccountWriteService;

@RequiredArgsConstructor
@RequestMapping("/api/accounts")
@RestController
public class AccountController {
    private final AccountWriteService accountWriteService;

    @PostMapping
    public void signUp(@Valid @RequestBody AccountSignUpRequest request) {
        accountWriteService.singUp(request.loginId(),request.nickname(),request.password(),
            PasswordHelper.createSalt());
    }

}

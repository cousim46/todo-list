package todo.list.security;

import static todo.list.exception.ErrorCode.EXPIRE_TOKEN_TIME;

import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import todo.list.api.controller.annotation.LoginUser;
import todo.list.api.controller.dto.request.LoginUserInfo;
import todo.list.domain.account.Account;
import todo.list.domain.account.AccountRepository;
import todo.list.exception.TodoListException;

@Component
@RequiredArgsConstructor
public class LoginInterception implements HandlerMethodArgumentResolver {

    private final AccountRepository accountRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginUser.class);
    }

    @Override
    public Object resolveArgument(
        MethodParameter parameter,
        ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest,
        WebDataBinderFactory binderFactory
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = accountRepository.findById(
            Long.parseLong(authentication.getName())
        ).orElseThrow(() -> new TodoListException(EXPIRE_TOKEN_TIME));
        return new LoginUserInfo(account.getId());
    }
}

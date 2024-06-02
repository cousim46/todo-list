package todo.list.api.controller.account.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AccountSignUpRequest(
    @NotBlank
    String loginId,
    @NotBlank
    String nickname,
    @NotBlank
    String password
) {
}

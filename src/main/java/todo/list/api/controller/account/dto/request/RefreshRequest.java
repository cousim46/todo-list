package todo.list.api.controller.account.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RefreshRequest(
    @NotBlank(message = "리프레쉬 토큰은 필수 값입니다.")
    String token
) {

}


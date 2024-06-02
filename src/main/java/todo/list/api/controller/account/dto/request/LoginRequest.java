package todo.list.api.controller.account.dto.request;

public record LoginRequest(
    String loginId,
    String password
){ }

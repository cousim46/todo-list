package todo.list.api.controller.todolist;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import todo.list.api.controller.annotation.LoginUser;
import todo.list.api.controller.dto.request.LoginUserInfo;
import todo.list.api.controller.todolist.dto.request.TodoCreateRequest;
import todo.list.api.service.todolist.TodoListWriteService;

@RequiredArgsConstructor
@Secured("ROLE_USER")
@RequestMapping("/api/todo")
@RestController
public class TodoListController {

    private final TodoListWriteService todoListWriteService;

    @PostMapping
    public void create(@LoginUser LoginUserInfo loginUserInfo,
        @Valid @RequestBody TodoCreateRequest request) {
        todoListWriteService.create(loginUserInfo.id(), request.title(), request.title());
    }
}

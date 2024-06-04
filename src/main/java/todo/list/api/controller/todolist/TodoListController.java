package todo.list.api.controller.todolist;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import todo.list.api.controller.annotation.LoginUser;
import todo.list.api.controller.dto.request.LoginUserInfo;
import todo.list.api.controller.dto.response.CommonResponse;
import todo.list.api.controller.todolist.dto.request.SearchRequest;
import todo.list.api.controller.todolist.dto.request.TodoCreateRequest;
import todo.list.api.controller.todolist.dto.request.TodoStatusUpdateRequest;
import todo.list.api.service.todolist.TodoListReadService;
import todo.list.api.service.todolist.TodoListWriteService;
import todo.list.api.service.todolist.dto.response.TodoListResponse;

@RequiredArgsConstructor
@Secured("ROLE_USER")
@RequestMapping("/api/todo")
@RestController
public class TodoListController {

    private final TodoListWriteService todoListWriteService;
    private final TodoListReadService todoListReadService;

    @PostMapping
    public void create(@LoginUser LoginUserInfo loginUserInfo,
        @Valid @RequestBody TodoCreateRequest request) {
        todoListWriteService.create(loginUserInfo.id(), request.title(), request.title());
    }

    @GetMapping
    public CommonResponse<List<TodoListResponse>> findAllOrRecent(@LoginUser LoginUserInfo loginUserInfo,
        @Valid SearchRequest request) {
        return CommonResponse.ok(
            todoListReadService.findAllOrRecent(request.type(), loginUserInfo.id())
        );
    }

    @PutMapping("/{todoId}")
    public void updateStatus(@LoginUser LoginUserInfo loginUserInfo, @PathVariable(name = "todoId") Long todoId, @RequestBody
        TodoStatusUpdateRequest request) {
        todoListWriteService.updateStatus(loginUserInfo.id(), todoId, request.status());
    }
}

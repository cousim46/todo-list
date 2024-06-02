package todo.list.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class CommonResponse<T> {
    private final int code;
    private final HttpStatus status;
    private T data;

    private CommonResponse(HttpStatus status, T data) {
        this.code = status.value();
        this.status = status;
        this.data = data;
    }

    public static <T>CommonResponse<T> of(HttpStatus status, T data) {
        return new CommonResponse<>(status, data);
    }
}



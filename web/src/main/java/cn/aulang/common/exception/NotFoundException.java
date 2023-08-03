package cn.aulang.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Data
@EqualsAndHashCode(callSuper = false)
public class NotFoundException extends ServiceException {

    @Serial
    private static final long serialVersionUID = 1L;

    private String id;

    public static NotFoundException of(Object id) {
        NotFoundException exception = new NotFoundException();
        exception.setId(String.valueOf(id));
        return exception;
    }

    public NotFoundException() {
        super();
    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
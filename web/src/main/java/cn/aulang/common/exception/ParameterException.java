package cn.aulang.common.exception;

import java.io.Serial;

public class ParameterException extends ServiceException {

    @Serial
    private static final long serialVersionUID = 1L;

    public ParameterException() {
        super();
    }

    public ParameterException(String message) {
        super(message);
    }

    public ParameterException(String message, Throwable cause) {
        super(message, cause);
    }
}

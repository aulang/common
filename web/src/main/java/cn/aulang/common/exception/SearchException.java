package cn.aulang.common.exception;

import java.io.Serial;

public class SearchException extends ServiceException {

    @Serial
    private static final long serialVersionUID = 1L;

    public SearchException() {
        super();
    }

    public SearchException(String message) {
        super(message);
    }

    public SearchException(String message, Throwable cause) {
        super(message, cause);
    }
}
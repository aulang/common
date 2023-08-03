package cn.aulang.common.exception;

import java.io.Serial;

public class SaveException extends RepositoryException {

    @Serial
    private static final long serialVersionUID = 1L;

    public SaveException() {
        super();
    }

    public SaveException(String message) {
        super(message);
    }

    public SaveException(String message, Throwable cause) {
        super(message, cause);
    }
}
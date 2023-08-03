package cn.aulang.common.exception;

import java.io.Serial;

public class RepositoryException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public RepositoryException() {
        super();
    }

    public RepositoryException(String message) {
        super(message);
    }

    public RepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
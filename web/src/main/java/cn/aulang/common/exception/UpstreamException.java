package cn.aulang.common.exception;

import java.io.Serial;

public class UpstreamException extends ServiceException {

    @Serial
    private static final long serialVersionUID = 1L;

    public UpstreamException() {
        super();
    }

    public UpstreamException(String message) {
        super(message);
    }

    public UpstreamException(String message, Throwable cause) {
        super(message, cause);
    }
}
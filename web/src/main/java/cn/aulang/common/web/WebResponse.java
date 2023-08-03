package cn.aulang.common.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebResponse<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private int code;
    private String msg;
    private T data;

    public WebResponse(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static <T> WebResponse<T> success() {
        return of(0, null, null);
    }

    public static <T> WebResponse<T> success(T data) {
        return of(0, null, data);
    }

    public static <T> WebResponse<T> fail() {
        return of(500, null, null);
    }

    public static <T> WebResponse<T> fail(int code) {
        return of(code, null, null);
    }

    public static <T> WebResponse<T> of(int code) {
        return of(code, null, null);
    }

    public static <T> WebResponse<T> of(int code, String msg) {
        return of(code, msg, null);
    }

    public static <T> WebResponse<T> of(int code, String msg, T data) {
        return new WebResponse<>(code, msg, data);
    }
}

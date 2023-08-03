package cn.aulang.common.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.io.PrintWriter;

/**
 * 当前用户拦截器
 *
 * @author wulang
 */
public class CurrentUserInterceptor implements HandlerInterceptor {

    private final AuthUserReader reader;

    public CurrentUserInterceptor(AuthUserReader reader) {
        this.reader = reader;
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                             @NonNull Object handler) throws Exception {
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            return true;
        }

        try {
            CurrentUser user = reader.read(request);
            UserContext.set(user);
        } catch (Exception e) {
            unauthorized(response, e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public void postHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                           @NonNull Object handler, ModelAndView modelAndView) {
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                @NonNull Object handler, @Nullable Exception ex) {
        UserContext.remove();
    }

    private void unauthorized(HttpServletResponse response, String message) throws Exception {
        if (response.isCommitted()) {
            return;
        }

        int unauthorized = HttpStatus.UNAUTHORIZED.value();

        response.setStatus(unauthorized);
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        try (PrintWriter writer = response.getWriter()) {
            writer.print("{\"code\":" + unauthorized + ", \"msg\":\"" + message + "\"}");
        }
    }
}

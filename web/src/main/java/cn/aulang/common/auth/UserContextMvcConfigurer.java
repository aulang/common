package cn.aulang.common.auth;

import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * 用户认证配置
 *
 * @author wulang
 */
public class UserContextMvcConfigurer implements WebMvcConfigurer {

    private final AuthUserReader reader;

    public UserContextMvcConfigurer(AuthUserReader reader) {
        this.reader = reader;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CurrentUserInterceptor(reader)).addPathPatterns("/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new CurrentUserResolver());
    }
}

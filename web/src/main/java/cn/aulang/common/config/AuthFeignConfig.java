package cn.aulang.common.config;

import cn.aulang.common.auth.CurrentUser;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Feign调用CurrentUser头部信息设置
 *
 * @author wulang
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(RequestInterceptor.class)
@ConditionalOnProperty(name = "auth-user.enabled", havingValue = "true", matchIfMissing = true)
public class AuthFeignConfig {

    @Bean
    public RequestInterceptor authContextInterceptor() {
        return template -> {
            if (RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attributes) {
                HttpServletRequest request = attributes.getRequest();

                setFeignHeader(CurrentUser.USER_ID, request, template);
                setFeignHeader(CurrentUser.USERNAME, request, template);
                setFeignHeader(CurrentUser.NICKNAME, request, template);
                setFeignHeader(CurrentUser.CLIENT_ID, request, template);
            }
        };
    }

    private void setFeignHeader(String headerKey, HttpServletRequest request, RequestTemplate template) {
        String headerValue = request.getHeader(headerKey);
        if (StringUtils.isNotBlank(headerValue)) {
            template.header(headerKey, headerValue);
        }
    }
}

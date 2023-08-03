package cn.aulang.common.config;

import cn.aulang.common.auth.AuthUserReader;
import cn.aulang.common.auth.GatewayAuthUserReader;
import cn.aulang.common.auth.UserContextMvcConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wulang
 */
@Configuration
@ConditionalOnProperty(name = "auth-user.enabled", havingValue = "true", matchIfMissing = true)
public class AuthUserConfig {

    @Bean
    @ConditionalOnMissingBean(AuthUserReader.class)
    public AuthUserReader authContextReader() {
        return new GatewayAuthUserReader();
    }

    @Bean
    public UserContextMvcConfigurer authContextMvcConfigurer(@Autowired AuthUserReader reader) {
        return new UserContextMvcConfigurer(reader);
    }
}

package message.security.config;

import message.mybatis.configs.annotations.EnableMyBatis;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2016-2-18 14:27
 */
@Configuration
@EnableMyBatis(
        dataSource = "security",
        basePackages = "message.security",
        useFlyway = true,
        migration = "db/security"
)
@ComponentScan(basePackages = {"message.security.repository", "message.security.service"})
public class SecurityConfig {
}

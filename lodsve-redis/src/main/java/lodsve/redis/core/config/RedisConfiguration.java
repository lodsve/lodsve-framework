package lodsve.redis.core.config;

import lodsve.core.autoconfigure.annotations.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2016-1-25 16:00
 */
@Configuration
@ComponentScan("lodsve.redis.core")
@EnableConfigurationProperties(RedisProperties.class)
public class RedisConfiguration {
}

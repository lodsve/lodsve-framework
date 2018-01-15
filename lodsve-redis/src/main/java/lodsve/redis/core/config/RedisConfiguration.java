package lodsve.redis.core.config;

import lodsve.core.properties.autoconfigure.annotations.EnableConfigurationProperties;
import lodsve.redis.core.properties.RedisProperties;
import org.springframework.context.annotation.Configuration;

/**
 * reids配置.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2016-1-25 16:00
 */
@Configuration
@EnableConfigurationProperties(RedisProperties.class)
public class RedisConfiguration {
}

package lodsve.cache.configs;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 不包含redis的缓存 Configuration.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2016/1/19 16:05
 */
@EnableCaching
@Configuration
@ComponentScan("lodsve.cache.core")
public class WithoutRedisCacheConfiguration {

}

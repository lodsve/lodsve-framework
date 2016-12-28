package lodsve.core;

import lodsve.core.appllication.ApplicationProperties;
import lodsve.core.autoconfigure.annotations.EnableConfigurationProperties;
import lodsve.core.email.EmailProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 配置core模块的properties.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2016/12/27 下午3:07
 */
@Configuration
@EnableConfigurationProperties({EmailProperties.class, ApplicationProperties.class})
public class CoreConfig {
}

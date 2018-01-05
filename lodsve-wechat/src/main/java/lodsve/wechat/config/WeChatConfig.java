package lodsve.wechat.config;

import lodsve.core.properties.autoconfigure.annotations.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * spring bean配置.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/24 上午12:07
 */
@Configuration
@ComponentScan("lodsve.wechat")
@EnableConfigurationProperties(WeChatProperties.class)
public class WeChatConfig {
}

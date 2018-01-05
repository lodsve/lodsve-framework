package lodsve.dfs.configuration;

import lodsve.core.properties.autoconfigure.annotations.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 配置.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2017-12-4-0004 11:18
 */
@Configuration
@EnableConfigurationProperties(DfsProperties.class)
public class DfsConfiguration {
}

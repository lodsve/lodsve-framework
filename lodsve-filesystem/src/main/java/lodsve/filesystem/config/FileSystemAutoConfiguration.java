package lodsve.filesystem.config;

import lodsve.core.autoproperties.relaxedbind.annotations.EnableConfigurationProperties;
import lodsve.filesystem.properties.FileSystemProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 文件上传组件配置.
 *
 * @author <a href="mailto:sun.hao278@iwhalecloud.com">sunhao(sun.hao278@iwhalecloud.com)</a>
 */
@Configuration
@EnableConfigurationProperties(FileSystemProperties.class)
public class FileSystemAutoConfiguration {
}

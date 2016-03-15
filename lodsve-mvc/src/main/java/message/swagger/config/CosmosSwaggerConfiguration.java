package message.swagger.config;

import com.mangofactory.swagger.plugin.EnableSwagger;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * 添加swagger部分的包扫描路径.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/1/18 下午11:05
 */
@Configuration
@EnableSwagger
@ComponentScan(basePackages = {"message.swagger"})
@Profile("swagger")
public class CosmosSwaggerConfiguration {
}

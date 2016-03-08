package message.swagger2.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


/**
 * 添加swagger部分的包扫描路径.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/1/18 下午11:05
 */
@Configuration
@EnableSwagger2
@ComponentScan(basePackages = {"message.swagger2"})
@Profile("swagger")
public class CosmosSwagger2Configuration {
}

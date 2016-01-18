package message.mvc.swagger;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 添加swagger部分的包扫描路径.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/1/18 下午11:05
 */
@Configuration
@ComponentScan(basePackages = {"message.mvc.swagger"})
public class CosmosSwaggerConfiguration {
}

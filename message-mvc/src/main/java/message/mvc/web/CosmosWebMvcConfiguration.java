package message.mvc.web;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 添加mvc部分的包扫描路径.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/1/18 下午11:06
 */
@Configuration
@ComponentScan(basePackages = {"message.mvc.web"})
public class CosmosWebMvcConfiguration {
}

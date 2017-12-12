package lodsve.validate.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * .
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version 1.0 2017/12/12 13:01
 */
@Configuration
@EnableAspectJAutoProxy
@ComponentScan("lodsve.validate")
public class ValidateConfiguration {
}

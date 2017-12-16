package lodsve.test.base;

import lodsve.core.configuration.EnableLodsve;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * test configuration.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2017/12/17 上午1:59
 */
@EnableLodsve
@Configuration
@ImportResource("classpath*:/META-INF/spring/*.xml")
public class TestConfiguration {
}

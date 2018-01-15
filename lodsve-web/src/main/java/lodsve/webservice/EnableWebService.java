package lodsve.webservice;

import lodsve.core.configuration.EnableLodsve;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * EnableWebService.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version 1.0 2018/1/13 下午11:55
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Inherited
@EnableLodsve
@Import({WebServiceConfiguration.class})
public @interface EnableWebService {
}

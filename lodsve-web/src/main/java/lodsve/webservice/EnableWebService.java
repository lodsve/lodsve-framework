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
@Import({WebserviceConfiguration.class, WebserviceBeanDefinitionRegistrar.class})
public @interface EnableWebService {
    WebService[] value() default {};

    public @interface WebService {
        Class<?> clazz() default Object.class;

        String id() default "";

        String address();
    }
}

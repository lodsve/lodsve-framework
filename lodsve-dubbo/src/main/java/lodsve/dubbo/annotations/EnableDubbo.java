package lodsve.dubbo.annotations;

import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启用dubbo注解.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2017-11-28-0028 12:41
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({DubboBeanDefinitionRegistrar.class})
public @interface EnableDubbo {

    /**
     * 配置生产者，作为消费方需要配置
     *
     * @return
     */
    String[] producers() default {};
}

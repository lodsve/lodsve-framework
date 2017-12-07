package lodsve.amqp.annotations;

import lodsve.amqp.configs.RabbitConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * rabbit mq base configuration.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2016-01-19 14:27
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Inherited
@Import(RabbitConfiguration.class)
public @interface EnableAmqp {
}

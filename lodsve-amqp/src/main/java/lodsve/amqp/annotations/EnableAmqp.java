package lodsve.amqp.annotations;

import lodsve.amqp.configs.RabbitConfiguration;
import lodsve.amqp.configs.RabbitProperties;
import lodsve.core.configuration.EnableLodsve;
import lodsve.core.properties.autoconfigure.annotations.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * rabbit mq base configuration.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 2016-01-19 14:27
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Inherited
@EnableLodsve
@EnableConfigurationProperties(RabbitProperties.class)
@Import(RabbitConfiguration.class)
public @interface EnableAmqp {
}

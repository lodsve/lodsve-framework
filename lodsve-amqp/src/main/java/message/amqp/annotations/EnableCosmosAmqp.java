package message.amqp.annotations;

import message.amqp.configs.RabbitConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

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
public @interface EnableCosmosAmqp {
}

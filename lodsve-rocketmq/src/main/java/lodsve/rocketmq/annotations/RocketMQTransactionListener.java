/*
 * Copyright (C) 2019 Sun.Hao(https://www.crazy-coder.cn/)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package lodsve.rocketmq.annotations;

import lodsve.rocketmq.configuration.RocketMQConfigUtils;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * This annotation is used over a class which implements interface
 * org.apache.rocketmq.client.producer.TransactionListener. The class implements
 * two methods for process callback events after the txProducer sends a transactional message.
 * <p>Note: The annotation is used only on RocketMQ client producer side, it can not be used
 * on consumer side.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 */
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface RocketMQTransactionListener {

    /**
     * Declare the txProducerGroup that is used to relate callback event to the listener, rocketMQTemplate must send a
     * transactional message with the declared txProducerGroup.
     * <p>
     * <p>It is suggested to use the default txProducerGroup if your system only needs to define a TransactionListener class.
     */
    String txProducerGroup() default RocketMQConfigUtils.ROCKETMQ_TRANSACTION_DEFAULT_GLOBAL_NAME;

    /**
     * Set ExecutorService params -- corePoolSize
     */
    int corePoolSize() default 1;

    /**
     * Set ExecutorService params -- maximumPoolSize
     */
    int maximumPoolSize() default 1;

    /**
     * Set ExecutorService params -- keepAliveTime
     */
    long keepAliveTime() default 1000 * 60; //60ms

    /**
     * Set ExecutorService params -- blockingQueueSize
     */
    int blockingQueueSize() default 2000;
}

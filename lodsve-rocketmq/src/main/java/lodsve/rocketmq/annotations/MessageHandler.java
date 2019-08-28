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

import lodsve.rocketmq.configuration.RocketMQProperties;

import java.lang.annotation.*;

/**
 * 标注这个类中含有注解{@link MessageListener}的方法都是消息的消费者.一个handle只能订阅一个topic
 *
 * @author 孙昊(Hulk)
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MessageHandler {

    /**
     * 消费者所属组，默认是在配置文件中配置的，详见{@link RocketMQProperties.Consumer#getGroup()}
     *
     * @return 消费者所属组
     */
    String group() default "${lodsve.rocketmq.consumer.group}";

    /**
     * 这个消息处理方法需要订阅的topic
     *
     * @return 需要订阅的topic
     */
    String topic();
}

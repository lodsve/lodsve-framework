/*
 * Copyright © 2009 Sun.Hao(https://www.crazy-coder.cn/)
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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lodsve.rocketmq.annotations;

import java.lang.annotation.*;

/**
 * 自定义的message listener，加在方法上（这些方法需要在一个类中，并且这个类要有{@link MessageHandler}这个注解），用来标注消息处理方法.
 *
 * @author 孙昊(Hulk)
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MessageListener {
    /**
     * 消息的标签（rocketmq的最佳实践：同一个项目只使用一个topic，不同类型的消息使用tag来进行区分）
     *
     * @return 消息的标签
     */
    String tag();
}

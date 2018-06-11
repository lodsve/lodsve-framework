/*
 * Copyright (C) 2018  Sun.Hao
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

package lodsve.amqp.core;

/**
 * 可以使用spel获取rabbit的一些配置bean name.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2017/12/7 13:30
 */
public class RabbitUtils {
    public static final String DEFAULT_EXCHANGE_BEAN_NAME = "defaultDirectExchange";
    public static final String DEFAULT_MESSAGE_CONVERTER_BEAN_NAME = "defaultMessageConverter";
    public static final String CONNECTION_FACTORY_BEAN_NAME = "connectionFactory";

    /**
     * default exchange bean name
     *
     * @return bean name
     */
    public static String defaultExchange() {
        return DEFAULT_EXCHANGE_BEAN_NAME;
    }

    /**
     * default message converter bean name
     *
     * @return bean name
     */
    public static String defaultMessageConverter() {
        return DEFAULT_MESSAGE_CONVERTER_BEAN_NAME;
    }

    /**
     * connection factory bean name
     *
     * @return bean name
     */
    public static String connectionFactory() {
        return CONNECTION_FACTORY_BEAN_NAME;
    }
}

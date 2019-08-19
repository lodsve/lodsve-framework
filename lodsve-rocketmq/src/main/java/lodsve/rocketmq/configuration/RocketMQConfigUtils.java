/*
 * Copyright (C) 2019  Sun.Hao
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

package lodsve.rocketmq.configuration;

/**
 * rocket配置的工具类.
 *
 * @author 孙昊(Hulk)
 */
public class RocketMQConfigUtils {
    /**
     * The bean name of the internally managed RocketMQ transaction annotation processor.
     */
    public static final String ROCKETMQ_TRANSACTION_ANNOTATION_PROCESSOR_BEAN_NAME =
            "org.springframework.rocketmq.spring.starter.internalRocketMQTransAnnotationProcessor";

    public static final String ROCKETMQ_TRANSACTION_DEFAULT_GLOBAL_NAME =
            "rocketmq_transaction_default_global_name";
}

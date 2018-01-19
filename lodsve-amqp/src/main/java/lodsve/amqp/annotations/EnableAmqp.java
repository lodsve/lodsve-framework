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

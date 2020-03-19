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

package lodsve.rocketmq.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import lodsve.core.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Jackson配置.
 *
 * @author 孙昊(Hulk)
 */
@Configuration
@ConditionalOnMissingBean(ObjectMapper.class)
class JacksonFallbackConfiguration {

    @Bean
    public ObjectMapper rocketMQMessageObjectMapper() {
        return new ObjectMapper();
    }

}

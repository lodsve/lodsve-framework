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

package lodsve.mongodb.config;

import lodsve.core.autoproperties.relaxedbind.annotations.EnableConfigurationProperties;
import lodsve.mongodb.Constants;
import lodsve.mongodb.core.MongoRepositoryBeanPostProcessor;
import lodsve.mongodb.properties.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;

/**
 * 基本配置.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 16/1/21 下午10:15
 */
@Configuration
@EnableAspectJAutoProxy
@EnableConfigurationProperties(MongoProperties.class)
@ComponentScan("lodsve.mongodb.connection")
public class MongoConfiguration {
    @Bean(name = Constants.DEFAULT_MONGO_TYPE_MAPPER_BEAN_NAME)
    public DefaultMongoTypeMapper defaultMongoTypeMapper() {
        return new DefaultMongoTypeMapper();
    }

    @Bean
    public MongoRepositoryBeanPostProcessor beanPostProcessor() {
        return new MongoRepositoryBeanPostProcessor();
    }
}

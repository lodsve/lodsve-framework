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

package lodsve.mybatis.configs;

import lodsve.core.properties.relaxedbind.annotations.EnableConfigurationProperties;
import lodsve.mybatis.properties.P6spyProperties;
import lodsve.mybatis.properties.RdbmsProperties;
import lodsve.mybatis.type.TypeHandlerScanner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * message-mybatis配置包扫描路径.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/1/19 下午10:21
 */
@Configuration
@EnableConfigurationProperties({RdbmsProperties.class, P6spyProperties.class})
@ComponentScan({"lodsve.mybatis.key", "lodsve.mybatis.datasource"})
@EnableAspectJAutoProxy
public class MyBatisConfiguration {
    @Bean
    public TypeHandlerScanner typeHandlerScanner() {
        return new TypeHandlerScanner();
    }
}

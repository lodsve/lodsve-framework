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

package lodsve.web.springfox.config;

import lodsve.core.properties.relaxedbind.annotations.EnableConfigurationProperties;
import lodsve.web.springfox.properties.SpringFoxProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 添加swagger部分的包扫描路径.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 16/3/23 下午4:16
 */
@Configuration
@EnableSwagger2
@EnableConfigurationProperties(SpringFoxProperties.class)
@ComponentScan(basePackages = {"lodsve.web.springfox"})
@Profile("springfox")
public class SpringFoxConfiguration {
}

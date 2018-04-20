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

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * swagger资源路径.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @date 16/3/23 下午4:17
 */
@Component
public class SpringFoxWebConfigurerAdapter extends WebMvcConfigurerAdapter {
    private static final String SPRING_FOX_UI_MAPPING = "/webjars/springfox-swagger-ui/**";
    private static final String SWAGGER_INDEX = "/swagger-ui.html";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if (!registry.hasMappingForPattern(SPRING_FOX_UI_MAPPING)) {
            registry.addResourceHandler(SPRING_FOX_UI_MAPPING).addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/").setCachePeriod(31556926);
        }
        if (!registry.hasMappingForPattern(SWAGGER_INDEX)) {
            registry.addResourceHandler(SWAGGER_INDEX).addResourceLocations("classpath:/META-INF/resources/swagger-ui.html").setCachePeriod(31556926);
        }
    }
}

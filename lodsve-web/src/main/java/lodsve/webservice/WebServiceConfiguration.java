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

package lodsve.webservice;

import lodsve.core.condition.ConditionalOnClass;
import lodsve.core.properties.relaxedbind.annotations.EnableConfigurationProperties;
import lodsve.webservice.properties.WebServiceProperties;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * WebserviceConfiguration.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version 1.0 2018/1/13 上午2:51
 */
@ConditionalOnClass(CXFServlet.class)
@Configuration
@EnableConfigurationProperties(WebServiceProperties.class)
@ImportResource({"classpath*:META-INF/cxf/cxf.xml", "classpath*:META-INF/cxf/cxf-servlet.xml"})
public class WebServiceConfiguration {

    @Bean
    public WebServiceBeanFactoryPostProcessor webServiceBeanFactoryPostProcessor() {
        return new WebServiceBeanFactoryPostProcessor();
    }

    @Bean
    public LodsveWebserviceInitializer lodsveWebserviceInitializer(WebServiceProperties properties) {
        LodsveWebserviceInitializer initializingBean = new LodsveWebserviceInitializer();
        initializingBean.setProperties(properties);

        return initializingBean;
    }
}

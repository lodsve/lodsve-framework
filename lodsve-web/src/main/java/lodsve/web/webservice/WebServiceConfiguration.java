/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package lodsve.web.webservice;

import lodsve.core.autoproperties.relaxedbind.annotations.EnableConfigurationProperties;
import lodsve.core.condition.ConditionalOnClass;
import lodsve.core.condition.ConditionalOnWebApplication;
import lodsve.web.webservice.properties.WebServiceProperties;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * WebserviceConfiguration.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2018/1/13 上午2:51
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
    @ConditionalOnWebApplication
    public LodsveWebserviceInitializer lodsveWebserviceInitializer(WebServiceProperties properties) {
        LodsveWebserviceInitializer initializingBean = new LodsveWebserviceInitializer();
        initializingBean.setProperties(properties);

        return initializingBean;
    }
}

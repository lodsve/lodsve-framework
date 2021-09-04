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

import lodsve.web.webservice.properties.ServletConfig;
import lodsve.web.webservice.properties.WebServiceProperties;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.ClassUtils;
import org.springframework.web.WebApplicationInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.util.Map;

/**
 * Lodsve Webservice Initializer.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2018/1/12 下午10:17
 */
public class LodsveWebserviceInitializer implements WebApplicationInitializer, InitializingBean {
    private static ServletContext servletContext;
    private WebServiceProperties properties;

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        LodsveWebserviceInitializer.servletContext = servletContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (!ClassUtils.isPresent(CXFServlet.class.getName(), this.getClass().getClassLoader())) {
            return;
        }

        CXFServlet servlet = new CXFServlet();
        ServletRegistration.Dynamic dynamic = servletContext.addServlet("CXFServlet", servlet);
        String path = properties.getPath();
        String urlMapping = (path.endsWith("/") ? path + "*" : path + "/*");
        dynamic.addMapping(urlMapping);

        ServletConfig servletProperties = this.properties.getServlet();
        dynamic.setLoadOnStartup(servletProperties.getLoadOnStartup());
        for (Map.Entry<String, String> entry : servletProperties.getInit().entrySet()) {
            dynamic.setInitParameter(entry.getKey(), entry.getValue());
        }
    }

    public void setProperties(WebServiceProperties properties) {
        this.properties = properties;
    }
}

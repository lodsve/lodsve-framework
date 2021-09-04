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
package lodsve.web.mvc.context;

import lodsve.core.autoproperties.relaxedbind.RelaxedBindFactory;
import lodsve.core.configuration.properties.ApplicationProperties;
import lodsve.core.utils.StringUtils;
import org.springframework.util.ClassUtils;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.util.Map;

/**
 * 设置spring项目编码.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2018-4-20-0020 11:52
 */
public class CharacterEncodingFilterInitializer implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        ApplicationProperties properties = new RelaxedBindFactory.Builder<>(ApplicationProperties.class).build();

        CharacterEncodingFilter filter = new CharacterEncodingFilter();

        FilterRegistration.Dynamic dynamic = servletContext.addFilter("encodingFilter", filter);
        dynamic.setInitParameter("encoding", properties.getEncoding());
        dynamic.setInitParameter("forceEncoding", Boolean.toString(properties.isForceEncoding()));

        try {
            String springDispatcherServletName = resolveSpringDispatcherServletName(servletContext);
            if (StringUtils.isBlank(springDispatcherServletName)) {
                throw new ServletException("No Spring Dispatcher Servlet Found in Spring Context!");
            }

            dynamic.addMappingForServletNames(null, false, springDispatcherServletName);
        } catch (ClassNotFoundException e) {
            throw new ServletException(e.getMessage());
        }
    }

    private String resolveSpringDispatcherServletName(ServletContext servletContext) throws ClassNotFoundException {
        Map<String, ? extends ServletRegistration> registrations = servletContext.getServletRegistrations();
        for (String key : registrations.keySet()) {
            ServletRegistration registration = registrations.get(key);
            String className = registration.getClassName();
            Class<?> clazz = ClassUtils.forName(className, getClass().getClassLoader());
            if (clazz.isAssignableFrom(DispatcherServlet.class)) {
                return key;
            }
        }

        return StringUtils.EMPTY;
    }
}

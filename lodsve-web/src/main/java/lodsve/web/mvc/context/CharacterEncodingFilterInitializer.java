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

package lodsve.web.mvc.context;

import lodsve.core.configuration.ApplicationProperties;
import lodsve.core.properties.relaxedbind.RelaxedBindFactory;
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

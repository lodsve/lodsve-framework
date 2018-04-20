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

package lodsve.web.webservice;

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
 * @author sunhao(sunhao.java @ gmail.com)
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

        WebServiceProperties.Servlet servletProperties = this.properties.getServlet();
        dynamic.setLoadOnStartup(servletProperties.getLoadOnStartup());
        for (Map.Entry<String, String> entry : servletProperties.getInit().entrySet()) {
            dynamic.setInitParameter(entry.getKey(), entry.getValue());
        }
    }

    public void setProperties(WebServiceProperties properties) {
        this.properties = properties;
    }
}

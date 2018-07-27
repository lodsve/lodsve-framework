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

package lodsve.web.utils;

import lodsve.core.properties.relaxedbind.RelaxedBindFactory;
import lodsve.web.mvc.properties.ServerProperties;
import org.springframework.web.WebApplicationInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * 注册验证码servlet.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2018-3-27-0027 11:22
 */
public class CaptchaInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        ServerProperties serverProperties = new RelaxedBindFactory.Builder<>(ServerProperties.class).build();

        if (!serverProperties.isEnableCaptcha()) {
            return;
        }

        CaptchaServlet servlet = new CaptchaServlet();
        ServletRegistration.Dynamic dynamic = servletContext.addServlet("CaptchaServlet", servlet);
        String urlMapping = serverProperties.getPath();
        dynamic.addMapping(urlMapping);

        CaptchaUtils.setServerProperties(serverProperties);
    }
}

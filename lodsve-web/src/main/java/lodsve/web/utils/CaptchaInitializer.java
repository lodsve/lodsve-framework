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
package lodsve.web.utils;

import lodsve.core.autoproperties.relaxedbind.RelaxedBindFactory;
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

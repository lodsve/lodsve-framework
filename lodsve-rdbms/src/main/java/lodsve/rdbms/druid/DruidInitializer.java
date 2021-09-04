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
package lodsve.rdbms.druid;

import com.alibaba.druid.support.http.StatViewServlet;
import lodsve.core.utils.StringUtils;
import lodsve.rdbms.properties.DruidProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.lang.NonNull;
import org.springframework.web.WebApplicationInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * 配置Druid监控页面.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2018-2-8-0008 14:54
 */
public class DruidInitializer implements WebApplicationInitializer, InitializingBean {
    private static ServletContext servletContext;
    private DruidProperties druidProperties;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (!druidProperties.isEnableMonitor()) {
            return;
        }

        StatViewServlet servlet = new StatViewServlet();
        ServletRegistration.Dynamic dynamic = servletContext.addServlet("DruidStatView", servlet);
        String path = druidProperties.getPath();
        String urlMapping = (path.endsWith("/") ? path + "*" : path + "/*");
        dynamic.addMapping(urlMapping);

        String allow = druidProperties.getAllow();
        if (StringUtils.isNotBlank(allow)) {
            dynamic.setInitParameter("allow", allow);
        }
        String deny = druidProperties.getDeny();
        if (StringUtils.isNotBlank(deny)) {
            dynamic.setInitParameter("deny", deny);
        }
        String remoteAddress = druidProperties.getRemoteAddress();
        if (StringUtils.isNotBlank(remoteAddress)) {
            dynamic.setInitParameter("remoteAddress", remoteAddress);
        }
        String resetEnable = druidProperties.getResetEnable();
        if (StringUtils.isNotBlank(resetEnable)) {
            dynamic.setInitParameter("resetEnable", resetEnable);
        }
        String loginUsername = druidProperties.getUser();
        if (StringUtils.isNotBlank(loginUsername)) {
            dynamic.setInitParameter("loginUsername", loginUsername);
        }
        String loginPassword = druidProperties.getPassword();
        if (StringUtils.isNotBlank(loginPassword)) {
            dynamic.setInitParameter("loginPassword", loginPassword);
        }
    }

    @Override
    public void onStartup(@NonNull ServletContext servletContext) throws ServletException {
        DruidInitializer.servletContext = servletContext;
    }

    public void setDruidProperties(DruidProperties druidProperties) {
        this.druidProperties = druidProperties;
    }
}

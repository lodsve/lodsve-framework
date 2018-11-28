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
    private final DruidProperties druidProperties;

    public DruidInitializer(DruidProperties druidProperties) {
        this.druidProperties = druidProperties;
    }

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
}

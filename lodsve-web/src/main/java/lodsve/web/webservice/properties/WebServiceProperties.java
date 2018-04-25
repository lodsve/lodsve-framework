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

package lodsve.web.webservice.properties;

import lodsve.core.properties.relaxedbind.annotations.ConfigurationProperties;
import org.springframework.util.Assert;

/**
 * 配置WebService的相关参数.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @date 16/1/23 下午8:50
 */
@ConfigurationProperties(prefix = "lodsve.webservice", locations = "${params.root}/framework/webservice.properties")
public class WebServiceProperties {

    /**
     * Path that serves as the base URI for the services.
     */
    private String path = "/services";

    private ServletConfig servlet = new ServletConfig();

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        Assert.notNull(path, "Path must not be null");
        Assert.isTrue(path.isEmpty() || path.startsWith("/"), "Path must start with / or be empty");
        this.path = path;
    }

    public ServletConfig getServlet() {
        return servlet;
    }

    public void setServlet(ServletConfig servlet) {
        this.servlet = servlet;
    }
}

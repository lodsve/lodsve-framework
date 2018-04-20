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

package lodsve.core.properties;

import lodsve.core.logger.Log4JConfiguration;
import lodsve.core.properties.env.EnvLoader;
import lodsve.core.properties.ini.IniLoader;
import lodsve.core.properties.profile.ProfileInitializer;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoader;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import static lodsve.core.properties.ParamsHome.PARAMS_HOME_NAME;

/**
 * 读取web.xml中设置的context-param[paramsHome].
 * 初始化配置文件的路径.<br/>
 * 配置文件加载顺序：<br/>
 * 环境变量 &gt; 容器启动参数 &gt; web.xml中配置<br/>
 * <ol>
 * <li>
 * web.xml中配置
 * <ul>
 * <li>
 * 配置context-param
 * <pre>
 *  &lt;context-param&gt;
 *     &lt;param-name&gt;paramsHome&lt;/param-name&gt;
 *     &lt;param-value&gt;your params home&lt;/param-value&gt;
 *  &lt;/context-param&gt;
 * </pre>
 * </li>
 * </ul>
 * </li>
 * <li>
 * 容器启动参数<br/>
 * {@code -Dparams.home=your params home }
 * </li>
 * <li>
 * 环境变量<br/>
 * 系统环境变量设置{@code PARAMS_HOME=you params home }
 * </li>
 * </ol>
 * 如果在classpath下,可以加上前缀classpath:you params home<br/>
 * 如果在zookeeper中,加上前缀zookeeper:you params home<br/>
 * 如果在文件系统中,可加前缀file:或者不加也行
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0
 * @createTime 2015-1-5 10:00
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ParamsHomeInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        String paramsHome = servletContext.getInitParameter(PARAMS_HOME_NAME);

        ParamsHome.getInstance().init(paramsHome);

        // 配置文件
        EnvLoader.init();
        IniLoader.init();

        // 配置log4j
        Log4JConfiguration.init();

        servletContext.setInitParameter(ContextLoader.CONTEXT_INITIALIZER_CLASSES_PARAM, ProfileInitializer.class.getName());
    }
}

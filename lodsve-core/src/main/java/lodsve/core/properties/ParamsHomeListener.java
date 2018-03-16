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

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

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
 * <li>
 * 配置listener[应该配置在web.xml中的所有listener之前，优先加载]
 * <pre>
 *  &lt;listener&gt;
 *      &lt;listener-class&gt;lodsve.core.properties.init.ParamsHomeListener&lt;/listener-class&gt;
 *  &lt;/listener&gt;
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
public class ParamsHomeListener implements ServletContextListener {
    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        ParamsHome.getInstance().cleanParamsRoot();
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        String paramsHome = servletContextEvent.getServletContext().getInitParameter(PARAMS_HOME_NAME);
        System.out.println(String.format("get init parameter '%s' from web.xml is '%s'", PARAMS_HOME_NAME, paramsHome));

        ParamsHome.getInstance().init(paramsHome);

        // 配置文件
        EnvLoader.init();
        IniLoader.init();

        // 配置log4j
        Log4JConfiguration.init();
    }
}

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
package lodsve.core.autoproperties;

import lodsve.core.ansi.AnsiOutput;
import lodsve.core.autoproperties.env.EnvLoader;
import lodsve.core.autoproperties.ini.IniLoader;
import lodsve.core.autoproperties.profile.ProfileInitializer;
import lodsve.core.autoproperties.relaxedbind.RelaxedBindFactory;
import lodsve.core.configuration.properties.ApplicationProperties;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoader;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

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
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2015-1-5 10:00
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ParamsHomeInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(@NonNull ServletContext servletContext) throws ServletException {
        String paramsHome = servletContext.getInitParameter(ParamsHome.PARAMS_HOME_NAME);

        ParamsHome.getInstance().init(paramsHome);

        // 配置文件
        EnvLoader.init();
        IniLoader.init();

        // 初始化ApplicationProperties
        ApplicationProperties properties = new RelaxedBindFactory.Builder<>(ApplicationProperties.class).build();
        AnsiOutput.Enabled enabled = properties.getAnsi().getEnabled();
        Boolean consoleAvailable = properties.getAnsi().getConsoleAvailable();
        AnsiOutput.setEnabled(enabled);
        AnsiOutput.setConsoleAvailable(consoleAvailable);

        servletContext.setInitParameter(ContextLoader.CONTEXT_INITIALIZER_CLASSES_PARAM, ProfileInitializer.class.getName());
    }
}

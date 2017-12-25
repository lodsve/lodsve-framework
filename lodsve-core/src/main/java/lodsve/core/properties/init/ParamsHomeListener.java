package lodsve.core.properties.init;

import lodsve.core.logger.Log4JConfiguration;
import lodsve.core.properties.env.EnvLoader;
import lodsve.core.properties.ini.IniLoader;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import static lodsve.core.properties.init.ParamsHome.PARAMS_HOME_NAME;

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
 *      &lt;listener-class&gt;message.config.init.ParamsHomeListener&lt;/listener-class&gt;
 *  &lt;/listener&gt;
 * </pre>
 * </li>
 * </ul>
 * </li>
 * <li>
 * 容器启动参数<br/>
 * {@code -params.home=your params home }
 * </li>
 * <li>
 * 环境变量<br/>
 * 系统环境变量设置{@code PARAMS_HOME=you params home }
 * </li>
 * </ol>
 * 如果在classpath下,可以加上前缀classpath:you params home<br/>
 * 如果在文件系统中,可加前缀system:或者不加也行
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

        // 配置log4j
        Log4JConfiguration.init();

        // 配置文件
        EnvLoader.init();
        IniLoader.init();
    }
}

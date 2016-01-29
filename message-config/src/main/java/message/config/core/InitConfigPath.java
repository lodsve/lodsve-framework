package message.config.core;

/**
 * 初始化配置文件的路径.<br/>
 * 配置文件加载顺序：<br/>
 * web.xml中配置 &gt; 启动参数 &gt; 环境变量<br/>
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
 *      &lt;listener-class&gt;message.config.core.ParamsHomeListener&lt;/listener-class&gt;
 *  &lt;/listener&gt;
 * </pre>
 * </li>
 * </ul>
 * </li>
 * <li>
 * 启动参数<br/>
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
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 14-9-2 下午11:53
 * @see message.config.core.ParamsHomeListener
 */
public class InitConfigPath {
    private static String paramsRoot = "";

    public static String getParamsRoot() {
        return paramsRoot;
    }

    private InitConfigPath() {
    }

    public static void setParamsRoot(String paramsRoot) {
        InitConfigPath.paramsRoot = paramsRoot;
    }
}

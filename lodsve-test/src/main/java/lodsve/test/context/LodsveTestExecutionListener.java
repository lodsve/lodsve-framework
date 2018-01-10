package lodsve.test.context;

import lodsve.core.logger.Log4JConfiguration;
import lodsve.core.properties.env.EnvLoader;
import lodsve.core.properties.ini.IniLoader;
import lodsve.core.properties.init.ParamsHome;
import lodsve.core.utils.StringUtils;
import lodsve.test.base.ParamsPath;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

/**
 * Lodsve TestExecutionListener.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version 1.0 2017/12/17 上午2:07
 */
public class LodsveTestExecutionListener implements TestExecutionListener {

    @Override
    public void beforeTestClass(TestContext testContext) throws Exception {
        initPath(testContext);

        ParamsHome.getInstance().init(StringUtils.EMPTY);
        // 配置文件
        EnvLoader.init();
        IniLoader.init();

        // 配置log4j
        Log4JConfiguration.init();
    }

    @Override
    public void prepareTestInstance(TestContext testContext) throws Exception {

    }

    @Override
    public void beforeTestMethod(TestContext testContext) throws Exception {

    }

    @Override
    public void afterTestMethod(TestContext testContext) throws Exception {
        ParamsHome.getInstance().cleanParamsRoot();
    }

    @Override
    public void afterTestClass(TestContext testContext) throws Exception {

    }

    private void initPath(TestContext testContext) {
        if (System.getProperties().keySet().contains(ParamsHome.JVM_PARAM_PATH)) {
            return;
        }

        Class<?> clazz = testContext.getTestClass();

        ParamsPath paramsPath;
        if (clazz.isAnnotationPresent(ParamsPath.class)) {
            paramsPath = clazz.getAnnotation(ParamsPath.class);
        } else {
            return;
        }

        String path = paramsPath.value();
        System.setProperty(ParamsHome.JVM_PARAM_PATH, path);
    }
}

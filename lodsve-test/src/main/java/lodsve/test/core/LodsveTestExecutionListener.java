package lodsve.test.core;

import lodsve.core.context.ApplicationHelper;
import lodsve.core.logger.Log4JConfiguration;
import lodsve.core.properties.core.ParamsHome;
import lodsve.core.properties.env.EnvLoader;
import lodsve.core.properties.ini.IniLoader;
import lodsve.core.utils.StringUtils;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

/**
 * Lodsve TestExecutionListener.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2017/12/17 上午2:07
 */
public class LodsveTestExecutionListener implements TestExecutionListener {

    @Override
    public void beforeTestClass(TestContext testContext) throws Exception {
        ParamsHome.getInstance().init(StringUtils.EMPTY);
        // 配置log4j
        Log4JConfiguration.init();

        // 配置文件
        EnvLoader.init();
        IniLoader.init();

        ApplicationHelper.getInstance().addApplicationContext(testContext.getApplicationContext());
    }

    @Override
    public void prepareTestInstance(TestContext testContext) throws Exception {
    }

    @Override
    public void beforeTestMethod(TestContext testContext) throws Exception {

    }

    @Override
    public void afterTestMethod(TestContext testContext) throws Exception {

    }

    @Override
    public void afterTestClass(TestContext testContext) throws Exception {

    }
}

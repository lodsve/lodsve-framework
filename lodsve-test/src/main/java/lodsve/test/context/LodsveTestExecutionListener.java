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

package lodsve.test.context;

import lodsve.core.properties.ParamsHome;
import lodsve.core.properties.env.EnvLoader;
import lodsve.core.properties.ini.IniLoader;
import lodsve.core.utils.StringUtils;
import lodsve.test.base.ParamsPath;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

/**
 * Lodsve TestExecutionListener.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2017/12/17 上午2:07
 */
public class LodsveTestExecutionListener implements TestExecutionListener {

    @Override
    public void beforeTestClass(TestContext testContext) throws Exception {
        initPath(testContext);

        ParamsHome.getInstance().init(StringUtils.EMPTY);
        // 配置文件
        EnvLoader.init();
        IniLoader.init();
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

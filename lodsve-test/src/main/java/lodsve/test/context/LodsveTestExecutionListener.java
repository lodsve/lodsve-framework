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
package lodsve.test.context;

import lodsve.core.autoproperties.ParamsHome;
import lodsve.core.autoproperties.env.EnvLoader;
import lodsve.core.autoproperties.ini.IniLoader;
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
        if (System.getProperties().containsKey(ParamsHome.JVM_PARAM_PATH)) {
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

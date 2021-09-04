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
package lodsve.test.mock.memcached;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.util.Assert;

/**
 * 内嵌式Memcached.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2018-1-10-0010 13:32
 */
public class JMemcachedTestExecutionListener extends AbstractTestExecutionListener {
    private static final Logger logger = LoggerFactory.getLogger(JMemcachedTestExecutionListener.class);
    public static final String MEMCACHED_HOST = "127.0.0.1";

    private static boolean initialized;
    private final JMemcachedServer server;

    public JMemcachedTestExecutionListener() {
        this.server = new JMemcachedServer();
    }

    @Override
    public void beforeTestClass(TestContext testContext) {
        startServer(testContext);
    }

    @Override
    public void afterTestMethod(TestContext testContext) {
        if (Boolean.TRUE.equals(testContext.getAttribute(DependencyInjectionTestExecutionListener.REINJECT_DEPENDENCIES_ATTRIBUTE))) {
            logger.debug("Cleaning and reloading server for test context [{}].", testContext);
            cleanServer();
            startServer(testContext);
        }
    }

    @Override
    public void afterTestClass(TestContext testContext) {
        cleanServer();
    }

    private void startServer(TestContext testContext) {
        EmbeddedMemcached embeddedMemcached = AnnotationUtils.findAnnotation(testContext.getTestClass(), EmbeddedMemcached.class);

        Assert.notNull(embeddedMemcached, "EmbeddedMemcachedTestExecutionListener must be used with @EmbeddedMemcached on " + testContext.getTestClass());

        int port = embeddedMemcached.port();
        Assert.isTrue(port > 0, "@EmbeddedMemcached port must not be > 0");

        if (!initialized) {
            server.start(MEMCACHED_HOST, port);
            initialized = true;
        }
    }

    private void cleanServer() {
        server.clean();
    }
}

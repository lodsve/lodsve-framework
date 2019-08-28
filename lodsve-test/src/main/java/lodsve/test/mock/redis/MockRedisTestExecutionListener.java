/*
 * Copyright (C) 2019 Sun.Hao(https://www.crazy-coder.cn/)
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

package lodsve.test.mock.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import redis.embedded.RedisServer;

import java.io.IOException;

/**
 * 内嵌式redis.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2018-1-10-0010 13:32
 */
public class MockRedisTestExecutionListener extends AbstractTestExecutionListener {
    private static final Logger logger = LoggerFactory.getLogger(MockRedisTestExecutionListener.class);

    private RedisServer server = null;

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
        EmbeddedRedis embeddedRedis = AnnotationUtils.findAnnotation(testContext.getTestClass(), EmbeddedRedis.class);
        int port = embeddedRedis.port();

        try {
            server = new RedisServer(port);
            server.start();
        } catch (IOException e) {
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    private void cleanServer() {
        server.stop();
        server = null;
    }
}

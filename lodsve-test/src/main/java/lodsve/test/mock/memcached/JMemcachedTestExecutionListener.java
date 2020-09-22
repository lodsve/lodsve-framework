/*
 * Copyright © 2009 Sun.Hao(https://www.crazy-coder.cn/)
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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
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

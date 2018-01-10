package lodsve.test.mock.memcached;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.springframework.util.Assert;

public class AbstractEmbeddedMemcachedTestExecutionListener extends AbstractTestExecutionListener {

    private static boolean initialized;
    private MemcachedServer server;

    public AbstractEmbeddedMemcachedTestExecutionListener(MemcachedServer server) {
        this.server = server;
    }

    void startServer(TestContext testContext) {
        EmbeddedMemcached embeddedMemcached = AnnotationUtils.findAnnotation(testContext.getTestClass(), EmbeddedMemcached.class);

        Assert.notNull(embeddedMemcached, "EmbeddedMemcachedTestExecutionListener must be used with @EmbeddedMemcached on " + testContext.getTestClass());

        String host = embeddedMemcached.host();
        int port = embeddedMemcached.port();
        Assert.isTrue(port > 0, "@EmbeddedMemcached port must not be > 0");

        if (!initialized) {
            server.start(host, port);
            initialized = true;
        }
    }

    void cleanServer() {
        server.clean();
    }
}

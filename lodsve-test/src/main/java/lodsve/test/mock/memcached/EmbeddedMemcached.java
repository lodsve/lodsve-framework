package lodsve.test.mock.memcached;

import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@Documented
@TestExecutionListeners({JMemcachedTestExecutionListener.class, DependencyInjectionTestExecutionListener.class})
public @interface EmbeddedMemcached {

    String host() default "127.0.0.1";

    int port() default 11211;
}

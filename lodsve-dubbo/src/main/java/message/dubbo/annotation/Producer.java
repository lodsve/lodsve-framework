package message.dubbo.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * dubbo生产方信息.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2016-2-4 15:49
 */
@Target({})
@Retention(RetentionPolicy.RUNTIME)
public @interface Producer {
    /**
     * 生产者application
     *
     * @return
     */
    String application();
}

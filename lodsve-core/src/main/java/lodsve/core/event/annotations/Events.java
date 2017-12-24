package lodsve.core.event.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 事件.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2017/12/24 下午10:39
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Events {
    /**
     * 同步事件
     *
     * @return 同步事件
     */
    SyncEvent[] sync() default {};

    /**
     * 同步事件
     *
     * @return 同步事件
     */
    AsyncEvent[] async() default {};
}

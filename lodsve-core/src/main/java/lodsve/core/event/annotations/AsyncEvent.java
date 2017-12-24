package lodsve.core.event.annotations;

import lodsve.core.event.module.BaseEvent;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 异步事件.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2017/12/24 下午10:39
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface AsyncEvent {
    /**
     * 事件中文名
     *
     * @return 事件中文名
     */
    String name();

    /**
     * 事件
     *
     * @return 事件
     */
    Class<? extends BaseEvent> event();
}

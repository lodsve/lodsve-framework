package message.redis.core.annotations;

import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/1/23 下午11:19
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(RedisImportSelector.class)
public @interface EnableRedis {
    /**
     * 数据源名称,required
     *
     * @return
     */
    String name();

    /**
     * 是否使用提供的定时器(使用的话需要配置数据源redisTimer)
     *
     * @return
     */
    boolean useTimer() default false;
}

package message.cache.configs;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识使用的缓存名称.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 14-8-29 下午9:10
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheType {
    public String value();
}

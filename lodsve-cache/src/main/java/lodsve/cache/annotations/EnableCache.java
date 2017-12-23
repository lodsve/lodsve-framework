package lodsve.cache.annotations;

import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启用cache模块.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2016/1/19 15:10
 * @see CacheImportSelector
 * @see CacheMode
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({CacheImportSelector.class})
public @interface EnableCache {
    /**
     * 选择使用的缓存类型
     *
     * @return
     * @see CacheMode
     */
    CacheMode cache() default CacheMode.REDIS;
}

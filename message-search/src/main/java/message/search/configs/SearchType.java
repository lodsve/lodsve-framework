package message.search.configs;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识使用的搜索引擎名称.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 14-9-14 下午3:39
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SearchType {
    public String value();
}

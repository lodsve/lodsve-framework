package lodsve.mongodb.connection;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据源提供者.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2017/12/14 下午5:33
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MongoSourceProvider {
    /**
     * 选择的数据源
     *
     * @return 数据源名称
     */
    String value();
}

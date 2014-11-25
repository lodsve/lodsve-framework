package message.jdbc.annontations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 重写@Column注解.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 13-1-26 下午9:17
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
    /**
     * 字段名
     *
     * @return
     */
    String name() default "";

    /**
     * 关联的外键所在表名或者映射class名
     *
     * @return
     */
    String foreignTable() default "";

    /**
     * 关联的外键名
     *
     * @return
     */
    String foreignKey() default "";

    boolean nullable() default true;

    int length() default 255;

    String type() default "";

    boolean unique() default true;
}

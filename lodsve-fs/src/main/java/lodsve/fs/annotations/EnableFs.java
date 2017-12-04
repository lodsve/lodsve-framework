package lodsve.fs.annotations;

import lodsve.fs.enums.FsType;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启用fs.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2017-12-4-0004 10:42
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Inherited
@Import(FsConfigurationSelector.class)
public @interface EnableFs {

    /**
     * 指定使用的文件系统类型{@link lodsve.fs.enums.FsType}，默认为FAST_DFS
     *
     * @return 类型
     */
    FsType value() default FsType.FAST_DFS;
}

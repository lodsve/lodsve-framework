package lodsve.dubbo.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 是否启用dubbo.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2016-2-4 13:27
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(DubboConfigurationRegistrar.class)
public @interface EnableDubbo {
    /**
     * 提供方应用信息，用于计算依赖关系
     *
     * @return
     */
    String application();

    /**
     * dubbo扫描的包路径
     *
     * @return
     */
    String[] scanPackages() default {};

    /**
     * 配置生产者
     *
     * @return
     */
    Producer[] producers() default {};
}

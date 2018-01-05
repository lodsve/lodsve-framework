package lodsve.core.properties.autoconfigure.annotations;

import lodsve.core.properties.autoconfigure.EnableConfigurationPropertiesImportSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启用配置.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2016/12/27 上午11:36
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(EnableConfigurationPropertiesImportSelector.class)
public @interface EnableConfigurationProperties {
    /**
     * 声明需要注册到spring上下文的properties类，否则不能使用@Autowired
     *
     * @return properties类
     */
    Class<?>[] value() default {};
}

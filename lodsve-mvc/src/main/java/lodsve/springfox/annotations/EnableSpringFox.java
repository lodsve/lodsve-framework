package lodsve.springfox.annotations;

import lodsve.springfox.config.SpringFoxConfiguration;
import org.springframework.context.annotation.Import;
import springfox.documentation.spring.web.paths.RelativePathProvider;

import java.lang.annotation.*;

/**
 * 启用lodsve-mvc中得spring fox.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/3/24 上午9:28
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({SpringFoxRegistrar.class, SpringFoxConfiguration.class})
public @interface EnableSpringFox {
    /**
     * url路径前缀
     *
     * @return 前缀
     */
    String prefix() default RelativePathProvider.ROOT;
}

package lodsve.springfox.annotations;

import lodsve.core.configuration.EnableLodsve;
import lodsve.springfox.config.SpringFoxConfiguration;
import org.springframework.context.annotation.Import;
import springfox.documentation.spring.web.paths.RelativePathProvider;
import springfox.documentation.spring.web.plugins.Docket;

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
@EnableLodsve
@Import({SpringFoxRegistrar.class, SpringFoxConfiguration.class})
public @interface EnableSpringFox {
    /**
     * url路径前缀
     *
     * @return 前缀
     */
    String prefix() default RelativePathProvider.ROOT;

    /**
     * 设置分组
     *
     * @return 分组
     */
    String[] groups() default Docket.DEFAULT_GROUP_NAME;
}

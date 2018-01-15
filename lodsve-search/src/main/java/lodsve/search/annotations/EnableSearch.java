package lodsve.search.annotations;

import lodsve.core.configuration.EnableLodsve;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启用搜索.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 2016/1/20 12:28
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableLodsve
@Import(SearchConfigurationSelector.class)
public @interface EnableSearch {
    /**
     * 所使用的搜索引擎类型
     *
     * @return
     */
    SearchType type() default SearchType.SOLR;
}

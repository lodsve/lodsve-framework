package lodsve.mybatis.configs.annotations;

import lodsve.mybatis.pagination.PaginationInterceptor;
import org.apache.ibatis.plugin.Interceptor;

import java.lang.annotation.*;

/**
 * mybatis插件描述.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2017/1/4 下午1:44
 */
@Target({})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Plugin {
    /**
     * 插件类
     *
     * @return 插件类
     */
    Class<? extends Interceptor> value() default PaginationInterceptor.class;

    /**
     * 插件所需要的参数
     *
     * @return
     */
    Param[] params() default {};

    /**
     * 插件所需要的参数
     */
    public @interface Param {
        /**
         * key
         *
         * @return
         */
        String key();

        /**
         * value
         *
         * @return
         */
        String value();
    }
}

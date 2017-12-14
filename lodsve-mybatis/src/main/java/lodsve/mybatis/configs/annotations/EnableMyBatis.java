package lodsve.mybatis.configs.annotations;

import lodsve.mybatis.configs.MyBatisConfigurationSelector;
import lodsve.mybatis.datasource.annotations.DataSource;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 通过注解的方式启动mybatis的配置.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/1/19 下午8:01
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(MyBatisConfigurationSelector.class)
public @interface EnableMyBatis {
    /**
     * 数据源名,多数据源
     *
     * @return
     */
    DataSource[] dataSource();

    /**
     * 是否支持事务
     *
     * @return
     */
    boolean supportTransaction() default false;

    /**
     * 含有{@link org.springframework.stereotype.Repository }注解的dao类所在的包路径,可以多个
     *
     * @return
     * @see org.springframework.stereotype.Repository
     */
    String[] basePackages() default {};

    /**
     * 枚举类型所在包路径,可以多个
     *
     * @return
     */
    String[] enumsLocations() default {};

    /**
     * 加载插件
     *
     * @return 插件
     */
    Plugin[] plugins() default {@Plugin};
}

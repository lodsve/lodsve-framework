package message.mybatis.configs.annotations;

import message.mybatis.configs.MyBatisConfigurationSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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
     * 数据源名
     *
     * @return
     */
    String dataSource();

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
     * 枚举类型转换器所在包路径,可以多个
     *
     * @return
     * @see message.mybatis.type.EnumCodeTypeHandler
     */
    String[] typeHandlersLocations() default {};

    /**
     * 主键生成策略,默认(false)是通过数据库生成,为true是按twitter的snowflake的ID生成器实现
     *
     * @return
     */
    boolean useSnowflake() default false;

    /**
     * 是否使用flyway
     *
     * @return
     */
    boolean useFlyway() default false;

    /**
     * flyway的脚本文件所在路径
     *
     * @return
     */
    String migration() default "db/migration";
}

package message.mongodb.annotations;

import message.mongodb.config.MongoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启用mongodb.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/1/21 下午10:14
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({MongoConfiguration.class, MongoBeanDefinitionRegistrar.class})
public @interface EnableMongo {
    /**
     * 数据源名
     *
     * @return
     */
    String dataSource();

    /**
     * 含有{@link org.springframework.stereotype.Repository }注解的dao类所在的包路径
     *
     * @return
     * @see org.springframework.stereotype.Repository
     */
    String[] basePackage();

    /**
     * 含有{@link org.springframework.data.mongodb.core.mapping.Document }注解的dao类所在的包路径
     *
     * @return
     * @see org.springframework.data.mongodb.core.mapping.Document
     */
    String[] domainPackage();
}

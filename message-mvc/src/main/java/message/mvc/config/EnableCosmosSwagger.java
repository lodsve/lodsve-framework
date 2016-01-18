package message.mvc.config;

import com.mangofactory.swagger.plugin.EnableSwagger;
import message.mvc.swagger.CosmosSwaggerConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启用cosmos-mvc中得swagger.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/1/18 下午10:50
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@EnableCosmosMvc
@EnableSwagger
@Import(CosmosSwaggerConfiguration.class)
public @interface EnableCosmosSwagger {
}

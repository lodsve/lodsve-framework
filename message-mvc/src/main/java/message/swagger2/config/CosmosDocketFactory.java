package message.swagger2.config;

import com.google.common.base.Predicate;
import message.properties.Swagger2Properties;
import message.swagger.annotations.SwaggerIgnore;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import static com.google.common.base.Predicates.not;
import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;
import static springfox.documentation.builders.RequestHandlerSelectors.withClassAnnotation;

/**
 * 生成springfox的Docket.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/3/8 下午9:39
 */
@Component("swaggerSpringMvcPlugin")
public class CosmosDocketFactory implements FactoryBean<Docket> {
    @Autowired
    private Swagger2Properties swagger2Properties;

    @Override
    public Docket getObject() throws Exception {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName(Docket.DEFAULT_GROUP_NAME)
                .select()
                .apis(not(withClassAnnotation(SwaggerIgnore.class)))
                .paths(paths())
                .build()
                .apiInfo(apiInfo());
    }

    @Override
    public Class<?> getObjectType() {
        return Docket.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    private Predicate<String> paths() {
        return or(regex(".*?"));
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                swagger2Properties.getTitle(),
                swagger2Properties.getDescription(),
                swagger2Properties.getVersion(),
                swagger2Properties.getTermsOfServiceUrl(),
                new Contact(swagger2Properties.getContact().getName(),
                        swagger2Properties.getContact().getUrl(),
                        swagger2Properties.getContact().getEmail()),
                swagger2Properties.getLicense(),
                swagger2Properties.getLicenseUrl()
        );
    }
}

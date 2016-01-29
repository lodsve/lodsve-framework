package message.swagger.config;

import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.mangofactory.swagger.plugin.SwaggerSpringMvcPlugin;
import com.wordnik.swagger.model.ApiInfo;
import message.properties.SwaggerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 利用插件配置swagger的一些信息.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/1/18 下午6:25
 */
@Component
public class CosmosSwaggerPlugin extends SwaggerSpringMvcPlugin {

    /**
     * Default constructor.
     * The argument springSwaggerConfig is used to by this class to establish sensible defaults.
     *
     * @param springSwaggerConfig
     */
    @Autowired
    public CosmosSwaggerPlugin(SpringSwaggerConfig springSwaggerConfig, SwaggerProperties swaggerProperties, CosmosPathProvider pathProvider) {
        super(springSwaggerConfig);

        apiInfo(getApiInfo(swaggerProperties));
        swaggerGroup("group");
        includePatterns(".*?");
        pathProvider(pathProvider);
    }

    private ApiInfo getApiInfo(SwaggerProperties swaggerProperties) {
        ApiInfo apiInfo = new ApiInfo(
                swaggerProperties.getTitle(),
                swaggerProperties.getDescription(),
                swaggerProperties.getTermsOfServiceUrl(),
                swaggerProperties.getContact(),
                swaggerProperties.getLicense(),
                swaggerProperties.getLicenseUrl()
        );

        return apiInfo;
    }
}

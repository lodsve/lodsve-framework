package message.mvc.swagger;

import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.mangofactory.swagger.plugin.SwaggerSpringMvcPlugin;
import com.wordnik.swagger.model.ApiInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * .
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
    public CosmosSwaggerPlugin(SpringSwaggerConfig springSwaggerConfig) {
        super(springSwaggerConfig);

        apiInfo(getApiInfo());
        swaggerGroup("group");
        includePatterns(".*?");
    }

    private ApiInfo getApiInfo() {
        ApiInfo apiInfo = new ApiInfo(
                "My Apps API Title",
                "My Apps API Description",
                "My Apps API terms of service",
                "My Apps API Contact Email",
                "My Apps API Licence Type",
                "My Apps API License URL"
        );

        return apiInfo;
    }
}

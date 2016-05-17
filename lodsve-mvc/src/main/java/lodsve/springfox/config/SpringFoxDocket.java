package lodsve.springfox.config;

import javax.annotation.PostConstruct;
import lodsve.base.utils.StringUtils;
import lodsve.properties.ApplicationProperties;
import lodsve.properties.SpringFoxProperties;
import lodsve.springfox.paths.SpringFoxPathProvider;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * 利用插件配置swagger的一些信息.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/3/24 上午9:30
 */
@Component
public class SpringFoxDocket extends Docket {
    @Autowired
    private SpringFoxPathProvider pathProvider;
    @Autowired
    private SpringFoxProperties properties;
    @Autowired
    private ApplicationProperties application;

    public SpringFoxDocket() {
        super(DocumentationType.SWAGGER_2);
    }

    @PostConstruct
    public void init() {
        apiInfo(apiInfo(properties));
        forCodeGeneration(true);
        groupName(DEFAULT_GROUP_NAME);
        pathProvider(pathProvider);
        host(getHost());
    }

    private ApiInfo apiInfo(SpringFoxProperties properties) {
        if (properties == null) {
            return ApiInfo.DEFAULT;
        }

        return new ApiInfo(
                properties.getTitle(),
                properties.getDescription(),
                "1.0",
                properties.getTermsOfServiceUrl(),
                new Contact("", "", properties.getContact()),
                properties.getLicense(),
                properties.getLicenseUrl()
        );
    }

    private String getHost() {
        String serverUrl = application.getServerUrl();
        if (StringUtils.isBlank(serverUrl)) {
            return "localhost";
        }
        String[] schemaAndHostAndPath = serverUrl.split("://");
        if (ArrayUtils.isEmpty(schemaAndHostAndPath) || 2 != ArrayUtils.getLength(schemaAndHostAndPath)) {
            return "localhost";
        }

        String[] hostAndPath = schemaAndHostAndPath[1].split("/");
        if (ArrayUtils.isEmpty(hostAndPath)) {
            return "localhost";
        }

        return hostAndPath[0];
    }
}

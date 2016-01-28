package message.swagger.config;

import com.mangofactory.swagger.controllers.DefaultSwaggerController;
import com.mangofactory.swagger.paths.SwaggerPathProvider;

/**
 * swagger路径配置.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/1/28 下午4:39
 */
public class CosmosPathProvider extends SwaggerPathProvider {
    private String prefix;

    public CosmosPathProvider(String prefix) {
        this.prefix = prefix;
    }

    @Override
    protected String applicationPath() {
        return prefix;
    }

    @Override
    protected String getDocumentationPath() {
        return "/" + DefaultSwaggerController.DOCUMENTATION_BASE_PATH;
    }
}

package message.swagger.paths;

import com.mangofactory.swagger.paths.RelativeSwaggerPathProvider;
import javax.servlet.ServletContext;
import message.base.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 处理swagger的路径.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/3 下午12:45
 */
public class CosmosSwaggerPathProvider extends RelativeSwaggerPathProvider {
    private String prefix;
    @Autowired
    private ServletContext servletContext;

    public CosmosSwaggerPathProvider(String prefix) {
        if (StringUtils.equals(ROOT, prefix)) {
            prefix = StringUtils.EMPTY;
        }
        this.prefix = prefix;
    }

    @Override
    protected String applicationPath() {
        String path = servletContext.getContextPath() + prefix;
        if (StringUtils.isEmpty(path)) {
            return ROOT;
        }

        return path;
    }

    @Override
    protected String getDocumentationPath() {
        return ROOT;
    }
}

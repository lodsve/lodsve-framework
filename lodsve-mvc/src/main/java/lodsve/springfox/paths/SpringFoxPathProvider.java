package lodsve.springfox.paths;

import javax.servlet.ServletContext;
import lodsve.base.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import springfox.documentation.spring.web.paths.RelativePathProvider;

/**
 * 处理swagger的路径.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/3/24 上午9:47
 */
public class SpringFoxPathProvider extends RelativePathProvider {
    private String prefix;
    private final ServletContext servletContext;

    @Autowired
    public SpringFoxPathProvider(ServletContext servletContext) {
        super(servletContext);
        this.servletContext = servletContext;
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

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}

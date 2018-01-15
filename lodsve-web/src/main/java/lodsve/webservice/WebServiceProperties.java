package lodsve.webservice;

import lodsve.core.properties.autoconfigure.annotations.ConfigurationProperties;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * 配置WebService的相关参数.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 16/1/23 下午8:50
 */
@ConfigurationProperties(prefix = "lodsve.webservice", locations = "${params.root}/framework/webservice.properties")
public class WebServiceProperties {

    /**
     * Path that serves as the base URI for the services.
     */
    private String path = "/services";

    private Servlet servlet = new Servlet();

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        Assert.notNull(path, "Path must not be null");
        Assert.isTrue(path.isEmpty() || path.startsWith("/"), "Path must start with / or be empty");
        this.path = path;
    }

    public Servlet getServlet() {
        return servlet;
    }

    public void setServlet(Servlet servlet) {
        this.servlet = servlet;
    }

    public static class Servlet {

        /**
         * Servlet init parameters to pass to Spring Web Services.
         */
        private Map<String, String> init = new HashMap<>();

        /**
         * Load on startup priority of the Spring Web Services servlet.
         */
        private int loadOnStartup = -1;

        public Map<String, String> getInit() {
            return this.init;
        }

        public void setInit(Map<String, String> init) {
            this.init = init;
        }

        public int getLoadOnStartup() {
            return this.loadOnStartup;
        }

        public void setLoadOnStartup(int loadOnStartup) {
            this.loadOnStartup = loadOnStartup;
        }

    }
}

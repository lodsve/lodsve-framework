package lodsve.properties;

import lodsve.core.properties.autoconfigure.annotations.ConfigurationProperties;

/**
 * web配置.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 2018-1-15-0015 16:51
 */
@ConfigurationProperties(prefix = "lodsve.web", locations = "${params.root}/framework/web.properties")
public class WebProperties {
    private ServerProperties server;
    private SpringFoxProperties springfox;
    private WebServiceProperties webservice;

    public ServerProperties getServer() {
        return server;
    }

    public void setServer(ServerProperties server) {
        this.server = server;
    }

    public SpringFoxProperties getSpringfox() {
        return springfox;
    }

    public void setSpringfox(SpringFoxProperties springfox) {
        this.springfox = springfox;
    }

    public WebServiceProperties getWebservice() {
        return webservice;
    }

    public void setWebservice(WebServiceProperties webservice) {
        this.webservice = webservice;
    }
}

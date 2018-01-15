package lodsve.springfox.properties;

import lodsve.core.properties.autoconfigure.annotations.ConfigurationProperties;

/**
 * 配置springfox的相关参数.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/1/23 下午8:50
 */
@ConfigurationProperties(prefix = "lodsve.springfox", locations = "${params.root}/framework/springfox.properties")
public class SpringFoxProperties {

    private String title;
    private String description;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

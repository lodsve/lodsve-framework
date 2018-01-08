package lodsve.mybatis.properties;

import lodsve.core.properties.autoconfigure.annotations.ConfigurationProperties;
import org.springframework.core.io.Resource;

/**
 * Spy Properties.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2017/12/25 下午11:53
 */
@ConfigurationProperties(prefix = "lodsve.p6spy", locations = "${params.root}/framework/spy.properties")
public class P6spyProperties {
    /**
     * spy配置
     */
    private Resource config;

    public Resource getConfig() {
        return config;
    }

    public void setConfig(Resource config) {
        this.config = config;
    }
}

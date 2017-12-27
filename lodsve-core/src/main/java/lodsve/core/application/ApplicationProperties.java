package lodsve.core.application;

import lodsve.core.autoconfigure.annotations.ConfigurationProperties;

/**
 * 系统配置.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2016/12/12 下午3:05
 */
@ConfigurationProperties(prefix = "application", locations = "file:${params.root}/framework/application.properties")
public class ApplicationProperties {
    /**
     * 开发模式
     */
    private boolean devMode;

    public boolean isDevMode() {
        return devMode;
    }

    public void setDevMode(boolean devMode) {
        this.devMode = devMode;
    }
}

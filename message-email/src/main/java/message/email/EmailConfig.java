package message.email;

import message.config.auto.annotations.ConfigurationProperties;

import java.util.Map;

/**
 * 邮箱服务器的配置.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 13-3-25 上午6:42
 */
@ConfigurationProperties(prefix = "cosmos.email", locations = "classpath:/META-INF/email.properties")
public class EmailConfig {
    private Map<String, EmailBean> beans;

    public Map<String, EmailBean> getBeans() {
        return beans;
    }

    public void setBeans(Map<String, EmailBean> beans) {
        this.beans = beans;
    }
}

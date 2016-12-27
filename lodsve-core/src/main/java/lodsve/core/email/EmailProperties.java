package lodsve.core.email;

import lodsve.core.autoconfigure.annotations.ConfigurationProperties;

import java.util.Map;

/**
 * 邮箱服务器的配置.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/5/26 下午2:57
 */
@ConfigurationProperties(prefix = "lodsve.email", locations = "classpath:/META-INF/email.properties")
public class EmailProperties {
    private Map<String, EmailBean> beans;

    public Map<String, EmailBean> getBeans() {
        return beans;
    }

    public void setBeans(Map<String, EmailBean> beans) {
        this.beans = beans;
    }
}

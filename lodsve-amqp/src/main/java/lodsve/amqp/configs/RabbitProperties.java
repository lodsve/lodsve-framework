package lodsve.amqp.configs;

import lodsve.core.properties.autoconfigure.annotations.ConfigurationProperties;

/**
 * rabbit mq base properties.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 2016-01-15 12:00
 */
@ConfigurationProperties(prefix = "lodsve.rabbit", locations = "${params.root}/framework/rabbit.properties")
public class RabbitProperties {
    private String address;
    private String username;
    private String password;
    private String defaultExchange;
    /**
     * Whether rejected deliveries are requeued by default; default true.
     */
    private Boolean defaultRequeueRejected;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDefaultExchange() {
        return defaultExchange;
    }

    public void setDefaultExchange(String defaultExchange) {
        this.defaultExchange = defaultExchange;
    }

    public Boolean getDefaultRequeueRejected() {
        return defaultRequeueRejected;
    }

    public void setDefaultRequeueRejected(Boolean defaultRequeueRejected) {
        this.defaultRequeueRejected = defaultRequeueRejected;
    }
}

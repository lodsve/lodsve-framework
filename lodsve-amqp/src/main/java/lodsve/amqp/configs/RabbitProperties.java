package lodsve.amqp.configs;

import lodsve.base.config.auto.annotations.ConfigurationProperties;

/**
 * rabbit mq base properties.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2016-01-15 12:00
 */
@ConfigurationProperties(prefix = "lodsve.rabbit")
public class RabbitProperties {
    private String address;
    private String username;
    private String password;
    private String exchange;

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

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }
}

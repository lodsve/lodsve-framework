package message.amqp.configs;

import message.config.auto.annotations.ConfigurationProperties;

/**
 * Created by sunhao on 2016/1/15.
 */
@ConfigurationProperties(prefix = "cosmos.rabbit")
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

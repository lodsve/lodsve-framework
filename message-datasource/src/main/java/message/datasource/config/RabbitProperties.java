package message.datasource.config;

import message.config.auto.annotations.ConfigurationProperties;

import java.util.Map;

/**
 * rabbit mq base properties.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2016-01-15 12:00
 */
@ConfigurationProperties(prefix = "cosmos.rabbit")
public class RabbitProperties {
    private String exchange;
    private Map<String, RabbitConnection> project;

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public Map<String, RabbitConnection> getProject() {
        return project;
    }

    public void setProject(Map<String, RabbitConnection> project) {
        this.project = project;
    }

    public static class RabbitConnection {
        private String address;
        private String username;
        private String password;

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
    }
}

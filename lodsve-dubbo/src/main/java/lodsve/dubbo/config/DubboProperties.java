package lodsve.dubbo.config;

import lodsve.core.autoconfigure.annotations.ConfigurationProperties;

import java.util.Map;

/**
 * dubbo配置.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2017-11-28-0028 12:42
 */
@ConfigurationProperties(prefix = "lodsve.dubbo", locations = "file:${params.root}/framework/dubbo.properties")
public class DubboProperties {
    private String application;
    private String protocol = "dubbo";
    private Integer port = 20800;
    private Integer threads = 200;
    private String registry;
    private Map<String, Address> producers;

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getThreads() {
        return threads;
    }

    public void setThreads(Integer threads) {
        this.threads = threads;
    }

    public String getRegistry() {
        return registry;
    }

    public void setRegistry(String registry) {
        this.registry = registry;
    }

    public Map<String, Address> getProducers() {
        return producers;
    }

    public void setProducers(Map<String, Address> producers) {
        this.producers = producers;
    }

    public static class Address {
        private String address;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }
}

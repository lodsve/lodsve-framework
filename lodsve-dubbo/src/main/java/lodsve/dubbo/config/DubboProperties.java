package lodsve.dubbo.config;

import lodsve.core.config.auto.annotations.ConfigurationProperties;

import java.util.Map;

/**
 * dubbo连接的一些资源.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2016-2-4 13:29
 */
@ConfigurationProperties(prefix = "lodsve.dubbo", locations = "file:${params.root}/framework/dubbo.properties")
public class DubboProperties {
    private Map<String, Registry> registry;

    public Map<String, Registry> getRegistry() {
        return registry;
    }

    public void setRegistry(Map<String, Registry> registry) {
        this.registry = registry;
    }

    public static class Registry{
        private String address;
        private Protocol protocol;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public Protocol getProtocol() {
            return protocol;
        }

        public void setProtocol(Protocol protocol) {
            this.protocol = protocol;
        }
    }

    public static class Protocol{
        private String name = "rmi";
        private int port = 20881;
        private String threadpool = "fixed";
        private int threads = 10;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getThreadpool() {
            return threadpool;
        }

        public void setThreadpool(String threadpool) {
            this.threadpool = threadpool;
        }

        public int getThreads() {
            return threads;
        }

        public void setThreads(int threads) {
            this.threads = threads;
        }
    }
}

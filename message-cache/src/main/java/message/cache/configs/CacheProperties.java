package message.cache.configs;

import message.config.auto.annotations.ConfigurationProperties;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/1/14 下午9:24
 */
@ConfigurationProperties(prefix = "cosmos.cache")
public class CacheProperties {
    private String cacheName = "ehcache";
    private Ehcache ehcache;
    private Memcache memcache;
    private Oscache oscache;

    public String getCacheName() {
        return cacheName;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    public Ehcache getEhcache() {
        return ehcache;
    }

    public void setEhcache(Ehcache ehcache) {
        this.ehcache = ehcache;
    }

    public Memcache getMemcache() {
        return memcache;
    }

    public void setMemcache(Memcache memcache) {
        this.memcache = memcache;
    }

    public Oscache getOscache() {
        return oscache;
    }

    public void setOscache(Oscache oscache) {
        this.oscache = oscache;
    }

    public static class Ehcache {
        private String configuration;

        public String getConfiguration() {
            return configuration;
        }

        public void setConfiguration(String configuration) {
            this.configuration = configuration;
        }
    }

    public static class Memcache {
        private String servers;

        public String getServers() {
            return servers;
        }

        public void setServers(String servers) {
            this.servers = servers;
        }
    }

    public static class Oscache {
        private String configuration;

        public String getConfiguration() {
            return configuration;
        }

        public void setConfiguration(String configuration) {
            this.configuration = configuration;
        }
    }
}

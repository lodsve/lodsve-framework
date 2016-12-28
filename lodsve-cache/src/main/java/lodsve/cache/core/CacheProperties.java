package lodsve.cache.core;

import lodsve.core.utils.StringUtils;
import lodsve.core.autoconfigure.annotations.ConfigurationProperties;

/**
 * cache配置.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/1/14 下午9:24
 */
@ConfigurationProperties(prefix = "lodsve.cache", locations = "file:${params.root}/framework/cache.properties")
public class CacheProperties {
    private Ehcache ehcache = new Ehcache();
    private String cacheNames = StringUtils.EMPTY;
    private Guava guava = new Guava();

    public Ehcache getEhcache() {
        return ehcache;
    }

    public void setEhcache(Ehcache ehcache) {
        this.ehcache = ehcache;
    }

    public String getCacheNames() {
        return cacheNames;
    }

    public void setCacheNames(String cacheNames) {
        this.cacheNames = cacheNames;
    }

    public Guava getGuava() {
        return guava;
    }

    public void setGuava(Guava guava) {
        this.guava = guava;
    }

    public static class Ehcache {
        private String configuration = StringUtils.EMPTY;

        public String getConfiguration() {
            return configuration;
        }

        public void setConfiguration(String configuration) {
            this.configuration = configuration;
        }
    }

    /**
     * Guava specific cache properties.
     */
    public static class Guava {
        private String spec = StringUtils.EMPTY;

        public String getSpec() {
            return this.spec;
        }

        public void setSpec(String spec) {
            this.spec = spec;
        }

    }
}

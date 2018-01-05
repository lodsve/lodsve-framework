package lodsve.cache.properties;

import lodsve.core.properties.autoconfigure.annotations.ConfigurationProperties;
import lodsve.core.utils.StringUtils;
import org.springframework.core.io.Resource;

/**
 * cache配置.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/1/14 下午9:24
 */
@ConfigurationProperties(prefix = "lodsve.cache", locations = "file:${params.root}/framework/cache.properties")
public class CacheProperties {
    private Ehcache ehcache = new Ehcache();
    private Guava guava = new Guava();
    private Redis redis = new Redis();

    public Ehcache getEhcache() {
        return ehcache;
    }

    public void setEhcache(Ehcache ehcache) {
        this.ehcache = ehcache;
    }

    public Redis getRedis() {
        return redis;
    }

    public void setRedis(Redis redis) {
        this.redis = redis;
    }

    public Guava getGuava() {
        return guava;
    }

    public void setGuava(Guava guava) {
        this.guava = guava;
    }

    public static class Ehcache {
        private Resource configuration;
        private EhcacheCache[] cache;

        public Resource getConfiguration() {
            return configuration;
        }

        public void setConfiguration(Resource configuration) {
            this.configuration = configuration;
        }

        public EhcacheCache[] getCache() {
            return cache;
        }

        public void setCache(EhcacheCache[] cache) {
            this.cache = cache;
        }

        public static class EhcacheCache {
            private String name;
            private long maxElementsInMemory = 10000;
            private boolean eternal = false;
            private long timeToIdleSeconds = 300;
            private long timeToLiveSeconds = 600;
            private boolean overflowToDisk = true;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public long getMaxElementsInMemory() {
                return maxElementsInMemory;
            }

            public void setMaxElementsInMemory(long maxElementsInMemory) {
                this.maxElementsInMemory = maxElementsInMemory;
            }

            public boolean getEternal() {
                return eternal;
            }

            public void setEternal(boolean eternal) {
                this.eternal = eternal;
            }

            public long getTimeToIdleSeconds() {
                return timeToIdleSeconds;
            }

            public void setTimeToIdleSeconds(long timeToIdleSeconds) {
                this.timeToIdleSeconds = timeToIdleSeconds;
            }

            public long getTimeToLiveSeconds() {
                return timeToLiveSeconds;
            }

            public void setTimeToLiveSeconds(long timeToLiveSeconds) {
                this.timeToLiveSeconds = timeToLiveSeconds;
            }

            public boolean getOverflowToDisk() {
                return overflowToDisk;
            }

            public void setOverflowToDisk(boolean overflowToDisk) {
                this.overflowToDisk = overflowToDisk;
            }
        }
    }

    /**
     * Guava specific cache properties.
     */
    public static class Guava extends Common {
        private String spec = StringUtils.EMPTY;

        public String getSpec() {
            return this.spec;
        }

        public void setSpec(String spec) {
            this.spec = spec;
        }

    }

    public static class Redis extends Common {

    }

    public static class Common {
        private String cacheNames = StringUtils.EMPTY;

        public String getCacheNames() {
            return cacheNames;
        }

        public void setCacheNames(String cacheNames) {
            this.cacheNames = cacheNames;
        }
    }
}

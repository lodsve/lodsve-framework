package message.cache.configs;

import message.cache.CacheManager;
import message.cache.memcached.MemcachedManagerImpl;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.spring.MemcachedClientFactoryBean;
import net.spy.memcached.transcoders.SerializingTranscoder;
import org.springframework.context.annotation.Configuration;

/**
 * Memcache Configuration.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2016/1/19 16:05
 */
@Configuration
public class MemcacheConfiguration extends CacheConfiguration {
    @Override
    protected CacheManager cacheManagerProvider() throws Exception {
        MemcachedManagerImpl memcachedManager = new MemcachedManagerImpl();

        memcachedManager.setDefaultTimeout(60);
        memcachedManager.setCacheGetTimeout(10);

        MemcachedClientFactoryBean clientFactoryBean = new MemcachedClientFactoryBean();
        clientFactoryBean.setServers(replaceValue(cacheProperties.getMemcache().getServers()));
        clientFactoryBean.setProtocol(ConnectionFactoryBuilder.Protocol.BINARY);
        SerializingTranscoder transcoder = new SerializingTranscoder();
        transcoder.setCompressionThreshold(1024);
        clientFactoryBean.setTranscoder(transcoder);
        clientFactoryBean.setOpTimeout(60000);
        clientFactoryBean.setTimeoutExceptionThreshold(1998);
        clientFactoryBean.setUseNagleAlgorithm(false);

        memcachedManager.setMemcachedClient((MemcachedClient) clientFactoryBean.getObject());

        return memcachedManager;
    }
}

package lodsve.redis.core.connection;

import lodsve.core.utils.StringUtils;
import lodsve.redis.core.properties.RedisProperties;
import lodsve.redis.exception.RedisException;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 继承重写redis数据源的工厂,实现自定义的创建连接的方法.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/9/8 下午23:40
 */
public class LodsveRedisConnectionFactory extends JedisConnectionFactory {
    private static final String URL_PREFIX = "redis://";
    private RedisProperties settings;

    LodsveRedisConnectionFactory(String dataSourceName, RedisProperties redisProperties) {
        settings = redisProperties;
        RedisProperties.ProjectSetting redisSetting = settings.getProject().get(dataSourceName);

        String url = redisSetting.getUrl();
        if (StringUtils.isBlank(url)) {
            throw new RedisException(103001, "url must not null");
        }
        if (!url.startsWith(URL_PREFIX)) {
            throw new RedisException(103002, "url needs to start with " + URL_PREFIX, URL_PREFIX);
        }

        url = url.substring(URL_PREFIX.length());
        String hostName = url.substring(0, url.indexOf(":"));
        String port = url.substring(url.indexOf(":") + 1, url.indexOf("/"));
        String dbIndex = url.substring(url.indexOf("/") + 1);

        String password = redisSetting.getPassword();
        if (StringUtils.isNotBlank(password)) {
            setPassword(redisSetting.getPassword());
        }
        setHostName(hostName);
        setPort(Integer.parseInt(port));
        setDatabase(Integer.parseInt(dbIndex));
        setPoolConfig(getJedisPoolConfig());
        setUsePool(true);

        int timeout = redisSetting.getTimeout();
        setTimeout(timeout);
    }

    private JedisPoolConfig getJedisPoolConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        RedisProperties.Pool props = settings.getPool();
        config.setMaxTotal(props.getMaxTotal());
        config.setMaxIdle(props.getMaxIdle());
        config.setMinIdle(props.getMinIdle());
        config.setMaxWaitMillis(props.getMaxWait());
        config.setTestOnBorrow(props.isTestOnBorrow());
        config.setTestOnReturn(props.isTestOnReturn());
        config.setTestWhileIdle(props.isTestWhileIdle());

        return config;
    }
}

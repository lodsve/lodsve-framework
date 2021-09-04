/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package lodsve.redis.core.connection;

import lodsve.core.utils.StringUtils;
import lodsve.redis.core.properties.PoolSetting;
import lodsve.redis.core.properties.ProjectRedisSetting;
import lodsve.redis.core.properties.RedisProperties;
import lodsve.redis.exception.RedisException;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 继承重写redis数据源的工厂,实现自定义的创建连接的方法.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 15/9/8 下午23:40
 */
public class LodsveRedisConnectionFactory extends JedisConnectionFactory {
    private static final String URL_PREFIX = "redis://";
    private final RedisProperties settings;

    LodsveRedisConnectionFactory(String dataSourceName, RedisProperties redisProperties) {
        settings = redisProperties;
        ProjectRedisSetting redisSetting = settings.getProject().get(dataSourceName);

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
        PoolSetting props = settings.getPool();
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

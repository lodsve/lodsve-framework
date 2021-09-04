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
package lodsve.cache.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lodsve.cache.properties.CacheProperties;
import lodsve.core.autoproperties.relaxedbind.annotations.EnableConfigurationProperties;
import lodsve.core.utils.StringUtils;
import lodsve.redis.core.annotations.EnableRedis;
import lodsve.redis.core.connection.LodsveRedisConnectionFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import java.util.Arrays;
import java.util.List;

/**
 * Redis.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2017/12/23 上午12:23
 */
@Configuration
@EnableCaching
@EnableConfigurationProperties(CacheProperties.class)
public class RedisCacheConfiguration {

    @Configuration
    @Import(RedisCacheBasicConfiguration.class)
    public static class RedisCacheManagerConfiguration {
        private final CacheProperties cacheProperties;
        private final RedisTemplate<Object, Object> redisTemplate;

        @Autowired
        public RedisCacheManagerConfiguration(ObjectProvider<CacheProperties> cacheProperties,
                                              @Qualifier("redisCacheRedisTemplate") ObjectProvider<RedisTemplate<Object, Object>> redisTemplate) {
            this.cacheProperties = cacheProperties.getIfAvailable();
            this.redisTemplate = redisTemplate.getIfAvailable();
        }

        @Bean
        public CacheManager cacheManager() {
            RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
            String cacheNames = cacheProperties.getRedis().getCacheNames();
            List<String> cacheNameList = Arrays.asList(StringUtils.split(cacheNames));

            if (!cacheNameList.isEmpty()) {
                cacheManager.setCacheNames(cacheNameList);
            }

            return cacheManager;
        }
    }

    @Configuration
    @EnableRedis(name = "cache")
    public static class RedisCacheBasicConfiguration {
        private final LodsveRedisConnectionFactory connectionFactory;

        public RedisCacheBasicConfiguration(@Qualifier("cache") ObjectProvider<LodsveRedisConnectionFactory> connectionFactory) {
            this.connectionFactory = connectionFactory.getIfAvailable();
        }

        @Bean
        public RedisTemplate<Object, Object> redisCacheRedisTemplate() {
            RedisTemplate<Object, Object> template = new RedisTemplate<>();

            template.setConnectionFactory(connectionFactory);

            Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
            ObjectMapper om = new ObjectMapper();
            om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
            om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
            jackson2JsonRedisSerializer.setObjectMapper(om);
            template.setKeySerializer(jackson2JsonRedisSerializer);
            template.setValueSerializer(jackson2JsonRedisSerializer);

            return template;
        }
    }
}

/*
 * Copyright (C) 2018  Sun.Hao
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package lodsve.redis.timer;

import lodsve.core.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * redis定时器的监听.<br/>
 * redis必须在配置文件redis.conf中设置为:<code>notify-keyspace-events Ex</code><br/>
 * also see <a href="http://blog.csdn.net/chaijunkun/article/details/27361453">Redis的Keyspace notifications功能初探</a>
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/9/28 下午4:11
 */
public class RedisTimerListener implements MessageListener {
    private static final Logger logger = LoggerFactory.getLogger(RedisTimerListener.class);
    private static final String REDIS_KEY_SEPARATOR = "-";
    private static final String REDIS_KEY_PREFIX = "redisEvent" + REDIS_KEY_SEPARATOR;
    private static final String REDIS_KEY_WORD = "__keyevent";

    @Autowired
    private ApplicationEventPublisher eventPublisher;
    private RedisTemplate<String, Object> redisTemplate;

    public RedisTimerListener(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        byte[] messageChannel = message.getChannel();
        byte[] messageBody = message.getBody();
        if (messageChannel == null || messageBody == null) {
            return;
        }
        String channel = new String(messageChannel);
        if (!channel.contains(REDIS_KEY_WORD)) {
            return;
        }
        String body = new String(messageBody);
        if (!body.startsWith(REDIS_KEY_PREFIX)) {
            return;
        }

        String[] temp = StringUtils.split(body, REDIS_KEY_SEPARATOR);

        String type = temp[1], key = temp[2];

        RedisEventHandler handler = RedisEventUtils.getRedisEventHandler(type);
        Serializable id = handler.resolveKey(key);

        if (id == null) {
            return;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Publishing Event for order " + id);
        }

        publishEvent(new RedisEvent(this, id, handler.getEventType()));
    }

    private void publishEvent(RedisEvent event) {
        try {
            this.eventPublisher.publishEvent(event);
        } catch (Throwable ex) {
            logger.error("Error publishing " + event + ".", ex);
        }
    }

    /**
     * 保存到redis
     *
     * @param key  唯一标示
     * @param ttl  失效时长(单位:秒)
     * @param type 事件类型
     */
    public void store(Serializable key, int ttl, RedisEventType type) {
        Assert.notNull(key, "key不能为空!");
        Assert.notNull(type, "type不能为空!");

        if (ttl <= 0) {
            // 抛出事件
            publishEvent(new RedisEvent(this, key, type));
            return;
        }

        BoundValueOperations<String, Object> operations = redisTemplate.boundValueOps(REDIS_KEY_PREFIX + type.getType() + REDIS_KEY_SEPARATOR + key);
        operations.set(key, ttl * 1000, TimeUnit.MILLISECONDS);
    }
}

package message.redis.timer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 根据类名解析处理类.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/9/29 下午12:53
 */
@Component
public class RedisEventUtils {
    private static Map<String, RedisEventHandler> handlers = new HashMap<>();

    @Autowired
    public RedisEventUtils(List<RedisEventHandler> handlers) {
        for (RedisEventHandler bean : handlers) {
            RedisEventType type = bean.getEventType();

            this.handlers.put(type.getType(), bean);
        }
    }

    public static RedisEventHandler getRedisEventHandler(String type) {
        return handlers.get(type);
    }
}

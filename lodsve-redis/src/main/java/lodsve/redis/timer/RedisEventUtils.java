package lodsve.redis.timer;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 根据类名解析处理类.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/9/29 下午12:53
 */
public class RedisEventUtils {
    private final static Map<String, RedisEventHandler> HANDLERS = new HashMap<>();

    @Autowired
    public RedisEventUtils(List<RedisEventHandler> handlers) {
        for (RedisEventHandler bean : handlers) {
            RedisEventType type = bean.getEventType();

            RedisEventUtils.HANDLERS.put(type.getType(), bean);
        }
    }

    public static RedisEventHandler getRedisEventHandler(String type) {
        return HANDLERS.get(type);
    }
}

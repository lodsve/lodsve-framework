package lodsve.redis.counter;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * 内存计数器.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/9/16 下午1:50
 */
@Component
public class MemoryCounter extends AbstractCounter {
    // 内存计数器
    private static final Map<String, Map<String, Integer>> COUNTER = new HashMap<>();

    @Override
    public void set(String catalog, String key, Integer times) {
        Assert.hasText(catalog, "目录不能为空!");
        Assert.hasText(key, "key不能为空!");
        Assert.notNull(times, "次数不能为空!");

        Map<String, Integer> catalogMap = COUNTER.get(catalog);
        if (catalogMap == null)
            catalogMap = new HashMap<>();

        catalogMap.put(key, times);
        COUNTER.put(catalog, catalogMap);
    }

    @Override
    public int get(String catalog, String key) {
        Assert.hasText(catalog, "目录不能为空!");
        Assert.hasText(key, "key不能为空!");

        Map<String, Integer> catalogMap = COUNTER.get(catalog);
        if (catalogMap == null) {
            return -1;
        }

        Integer time = catalogMap.get(key);
        return time != null ? time : -1;
    }

    @Override
    public void _increment(String catalog, String key, int step) {
        Assert.hasText(catalog, "目录不能为空!");
        Assert.hasText(key, "key不能为空!");

        int time = get(catalog, key);
        time += step;
        set(catalog, key, time);
    }

    @Override
    public void flushAll() {
        COUNTER.clear();
    }

    @Override
    public void flush(String catalog) {
        Assert.hasText(catalog, "目录不能为空!");

        Map<String, Integer> catalogCounter = COUNTER.get(catalog);
        if (catalogCounter != null) {
            catalogCounter.clear();
        }
    }
}

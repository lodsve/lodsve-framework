package lodsve.base.utils;

import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 参数放入ThreadLocal中,同一线程中,不用传递参数,即可使用.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/10/27 下午3:10
 */
public class ParamsHolder {
    private ParamsHolder() {
    }

    private static ThreadLocal<Map<String, Object>> paramsHolder = new ThreadLocal<>();

    public static void set(String key, Object object) {
        Assert.hasText(key);

        Map<String, Object> params = paramsHolder.get();
        if (params == null) {
            params = new HashMap<>();
        }

        params.put(key, object);

        paramsHolder.set(params);
    }

    public static <T> T get(String key) {
        Map<String, Object> params = paramsHolder.get();
        if (CollectionUtils.isEmpty(params)) {
            return null;
        }

        return (T) params.get(key);
    }

    public static void sets(Map<String, Object> params) {
        paramsHolder.set(params);
    }

    public static Map<String, Object> gets() {
        return paramsHolder.get();
    }
}

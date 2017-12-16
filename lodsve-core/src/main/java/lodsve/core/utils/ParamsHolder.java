package lodsve.core.utils;

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

    private static final ThreadLocal<Map<String, Object>> PARAMS_HOLDER = new ThreadLocal<>();

    public static void set(String key, Object object) {
        Assert.hasText(key);

        Map<String, Object> params = PARAMS_HOLDER.get();
        if (params == null) {
            params = new HashMap<>(1);
        }

        params.put(key, object);

        PARAMS_HOLDER.set(params);
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(String key) {
        Map<String, Object> params = PARAMS_HOLDER.get();
        if (CollectionUtils.isEmpty(params)) {
            return null;
        }

        return (T) params.get(key);
    }

    public static void remove(String key) {
        Map<String, Object> params = PARAMS_HOLDER.get();
        if (CollectionUtils.isEmpty(params)) {
            return;
        }

        params.remove(key);
    }

    public static void removes() {
        PARAMS_HOLDER.remove();
    }

    public static void sets(Map<String, Object> params) {
        PARAMS_HOLDER.set(params);
    }

    public static Map<String, Object> gets() {
        return PARAMS_HOLDER.get();
    }
}

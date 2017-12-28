package lodsve.core.json;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Gson Utils.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 2017-12-28-0028 14:57
 */
public class GsonConverter implements JsonConverter {
    private static final Gson gson;

    static {
        gson = new Gson();
    }

    @Override
    public String toJson(Object obj) {
        return gson.toJson(obj);
    }

    @Override
    public <T> T toObject(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

    @Override
    public Map<String, Object> toMap(String json) {
        Type mapType = new TypeToken<Map<String, Object>>() {
        }.getType();

        return gson.fromJson(json, mapType);
    }
}

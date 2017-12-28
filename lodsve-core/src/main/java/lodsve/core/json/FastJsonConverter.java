package lodsve.core.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * FastJson Utils.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 2017-12-28-0028 14:43
 */
public class FastJsonConverter implements JsonConverter {
    @Override
    public String toJson(Object obj) {
        Assert.notNull(obj);

        return JSON.toJSONString(obj);
    }

    @Override
    public <T> T toObject(String json, Class<T> clazz) {
        Assert.hasText(json);
        Assert.notNull(clazz);

        return JSON.parseObject(json, clazz);
    }

    @Override
    public Map<String, Object> toMap(String json) {
        return JSON.parseObject(json, new TypeReference<Map<String, Object>>() {
        });
    }
}

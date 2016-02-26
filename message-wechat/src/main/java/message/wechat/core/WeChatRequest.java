package message.wechat.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import message.base.utils.StringUtils;
import message.wechat.exception.WeChatException;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/21 下午5:26
 */
public final class WeChatRequest {
    private static final Logger logger = LoggerFactory.getLogger(WeChatRequest.class);
    private static final RestTemplate template = new RestTemplate();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static <T> T get(String url, Class<T> responseType, Object... params) {
        Map<String, Object> result = template.getForObject(url, Map.class, params);
        if (isError(result)) {
            throw new WeChatException((Integer) result.get("errcode"), (String) result.get("errmsg"));
        }

        return evalMap(result, responseType);
    }

    public static <T> T post(String url, Object object, Class<T> responseType, Object... params) {
        Map<String, Object> result = template.postForObject(url, object, Map.class, params);
        if (isError(result)) {
            throw new WeChatException((Integer) result.get("errcode"), (String) result.get("errmsg"));
        }

        return evalMap(result, responseType);
    }

    private static boolean isError(Map<String, Object> result) {
        if (MapUtils.isEmpty(result)) {
            return true;
        }

        Object errcode = result.get("errcode");
        String errcode_ = errcode == null ? StringUtils.EMPTY : errcode.toString();

        return !("".equals(errcode_) || "0".equals(errcode_));

    }

    public static <T> T evalMap(Map<String, Object> result, Class<T> responseType) {
        try {
            String json = OBJECT_MAPPER.writeValueAsString(result);
            return OBJECT_MAPPER.readValue(json, responseType);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }
}

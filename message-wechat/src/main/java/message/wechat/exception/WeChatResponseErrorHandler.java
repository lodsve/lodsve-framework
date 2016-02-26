package message.wechat.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.client.ResponseErrorHandler;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/25 上午10:02
 */
public class WeChatResponseErrorHandler implements ResponseErrorHandler {
    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return isError(evalException(response));
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        Map<String, String> exception = evalException(response);
        throw new WeChatException(Integer.valueOf(exception.get("errcode")), exception.get("errmsg"));
    }

    private Map<String, String> evalException(ClientHttpResponse response) throws IOException {
        String message = getMessage(response.getBody());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        JavaType javaType = objectMapper.getTypeFactory().constructParametrizedType(Map.class, HashMap.class, String.class, String.class);

        return objectMapper.readValue(message, javaType);
    }

    private String getMessage(InputStream body) {
        try {
            return IOUtils.toString(body);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    private boolean isError(Map<String, String> message) {
        if (MapUtils.isEmpty(message)) {
            return true;
        }

        String errcode = message.get("errcode");

        if(StringUtils.isEmpty(errcode) || "0".equals(message.get("errcode"))) {
            return false;
        }

        return true;
    }
}

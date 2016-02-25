package message.wechat.core;

import java.util.Collections;
import message.mvc.convert.CustomMappingJackson2HttpMessageConverter;
import message.mvc.convert.CustomObjectMapper;
import message.wechat.exception.WeChatResponseErrorHandler;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/21 下午5:26
 */
public final class WeChatRequest {
    private static final RestTemplate template = new RestTemplate();

    static {
        CustomMappingJackson2HttpMessageConverter converter = new CustomMappingJackson2HttpMessageConverter();
        converter.setObjectMapper(new CustomObjectMapper());

        template.setMessageConverters(Collections.<HttpMessageConverter<?>>singletonList(converter));

        template.setErrorHandler(new WeChatResponseErrorHandler());
    }

    public static <T> T get(String url, Class<T> responseType, Object... params) {
        return template.getForObject(url, responseType, params);
    }

    public static <T> T post(String url, Object object, Class<T> responseType, Object... params) {
        return template.postForObject(url, object, responseType, params);
    }
}

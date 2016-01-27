package message.wechat.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestTemplate;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2016-1-27 14:24
 */
public class BaseRequest {
    private RestTemplate template = new RestTemplate();
    private ObjectMapper mapper = new ObjectMapper();
}

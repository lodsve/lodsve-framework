package message.mvc.resolver;

import message.mvc.annotation.Resolver;
import message.mvc.commons.WebOutput;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 解析WebOutput.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 2014-12-19 19:33
 */
@Resolver(WebOutput.class)
@Component
public class WebOutputResolver implements HandlerResolver<WebOutput> {
    @Override
    public WebOutput resolver(HttpServletRequest request, HttpServletResponse response) {
        if(response != null && request != null) {
            return new WebOutput(request, response);
        }

        return null;
    }
}

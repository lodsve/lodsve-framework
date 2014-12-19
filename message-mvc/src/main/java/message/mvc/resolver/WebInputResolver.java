package message.mvc.resolver;

import message.mvc.annotation.Resolver;
import message.mvc.commons.WebInput;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 解析WebInput.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 2014-12-19 19:16
 */
@Resolver(WebInput.class)
@Component
public class WebInputResolver implements HandlerResolver<WebInput> {
    @Override
    public WebInput resolver(HttpServletRequest request, HttpServletResponse response) {
        if(request != null)
            return new WebInput(request);

        return null;
    }
}

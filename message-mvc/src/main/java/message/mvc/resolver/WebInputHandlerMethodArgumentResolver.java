package message.mvc.resolver;

import message.mvc.annotation.Inject;
import message.mvc.commons.WebInput;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

/**
 * 为controller注入参数WebInput.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 2015-1-29 21:37
 */
public class WebInputHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Inject inject = parameter.getParameterAnnotation(Inject.class);
        Class<?> paramType = parameter.getParameterType();

        return inject != null && WebInput.class.equals(inject.value()) && WebInput.class.isAssignableFrom(paramType);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

        if(request != null) {
            return new WebInput(request);
        }

        return null;
    }
}

package message.mvc.resolver;

import message.mvc.annotation.Inject;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 2014-12-19 13:28
 */
public class MessageMVCHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Inject inject = parameter.getParameterAnnotation(Inject.class);
        return inject != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
        Class<?> clazz = parameter.getParameterType();

        HandlerResolver resolver = MethodArgumentResolverFactory.getResolver(clazz);
        if(resolver == null) {
            return null;
        }

        return resolver.resolver(request, response);
    }
}

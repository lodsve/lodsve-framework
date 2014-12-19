package message.mvc.resolver;

import message.mvc.annotation.Inject;
import message.mvc.commons.FileWebInput;
import message.mvc.commons.WebInput;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

/**
 * 解析WebInput参数.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 2014-12-19 10:58
 */
public class WebInputResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Inject wi = parameter.getParameterAnnotation(Inject.class);
        Class<?> clazz = parameter.getParameterType();

        return wi != null && WebInput.class.isAssignableFrom(clazz);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

        if(request == null) {
            return null;
        }

        Class<?> clazz = parameter.getParameterType();
        if(FileWebInput.class.equals(clazz)) {
            return new FileWebInput(request);
        }

        return new WebInput(request);
    }
}

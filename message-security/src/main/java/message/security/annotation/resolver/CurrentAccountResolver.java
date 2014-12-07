package message.security.annotation.resolver;

import message.security.SecurityConstants;
import message.security.annotation.CurrentAccount;
import message.security.pojo.Account;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 解析参数.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 2014-12-7 19:49
 */
public class CurrentAccountResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        CurrentAccount ca = parameter.getParameterAnnotation(CurrentAccount.class);
        Class<?> clazz = parameter.getParameterType();

        return ca != null && Account.class.isAssignableFrom(clazz);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute(SecurityConstants.ACCOUNT_KEY_IN_SESSION);

        return account;
    }
}

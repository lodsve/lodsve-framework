/*
 * Copyright (C) 2019 Sun.Hao(https://www.crazy-coder.cn/)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package lodsve.web.mvc.annotation.resolver;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lodsve.core.utils.ObjectUtils;
import lodsve.core.utils.StringUtils;
import lodsve.web.mvc.annotation.Bind;
import lodsve.web.mvc.commons.WebInput;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 解析加了注解{@link Bind}的参数.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2015-1-29 21:49
 */
public class BindDataHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Bind bind = parameter.getParameterAnnotation(Bind.class);

        return bind != null && StringUtils.isNotEmpty(bind.value());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        //前缀
        String name = parameter.getParameterAnnotation(Bind.class).value();
        //类型
        Class<?> clazz = parameter.getParameterType();
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

        //所有参数
        Enumeration<String> es = request.getParameterNames();
        Map<String, String> paramsMap = new HashMap<>(16);
        for (; es.hasMoreElements(); ) {
            String key = es.nextElement();
            String value = request.getParameter(key);

            if (key.startsWith(name + ".")) {
                String field = StringUtils.removeStart(key, name + ".");
                paramsMap.put(field, value);
            }
        }

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        //转成json
        String paramsJson = objectMapper.writeValueAsString(paramsMap);

        WebInput in = new WebInput(request);
        Object normalValue = in.getBean(clazz);
        Object jacksonValue = objectMapper.readValue(paramsJson, clazz);

        return ObjectUtils.mergerObject(jacksonValue, normalValue);
    }
}

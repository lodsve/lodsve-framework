/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

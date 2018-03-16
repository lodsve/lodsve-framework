/*
 * Copyright (C) 2018  Sun.Hao
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

package lodsve.mvc.resolver;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;
import lodsve.core.utils.StringParse;
import lodsve.core.utils.StringUtils;
import lodsve.mvc.annotation.Parse;
import lodsve.mvc.commons.WebInput;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 当前台传入类似于1,2,3,4,5之类的字符串，将会被解析成一个List，具体参数在{@link Parse}中配置.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 2015-2-9 18:59
 * @see Parse
 */
public class ParseDataHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    private static final Logger logger = LoggerFactory.getLogger(ParseDataHandlerMethodArgumentResolver.class);
    private final static Map<String, StringParse> MAPPERS = new HashMap<>();
    private final static ClassPool CLASSPOOL;

    static {
        ClassPool parent = ClassPool.getDefault();
        ClassPool child = new ClassPool(parent);
        child.appendClassPath(new LoaderClassPath(ParseDataHandlerMethodArgumentResolver.class.getClassLoader()));
        child.appendSystemPath();
        child.childFirstLookup = true;
        CLASSPOOL = child;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Parse parse = parameter.getParameterAnnotation(Parse.class);
        Class<?> clazz = parameter.getParameterType();

        //获取泛型
        Class<?> genricType = this.getClassGenricType(parameter);

        return ClassUtils.isAssignable(List.class, clazz) && parse != null && parse.dest().equals(genricType);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Parse parse = parameter.getParameterAnnotation(Parse.class);
        Class<?> dest = parse.dest();

        //request中的key
        String key = parse.value();
        key = (StringUtils.isEmpty(key) ? parameter.getParameterName() : key);

        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        WebInput in = new WebInput(request);
        String valueInRequest = in.getString(key, StringUtils.EMPTY);

        if (StringUtils.isEmpty(valueInRequest)) {
            return null;
        }

        String delimiters = parse.delimiters();

        String parseObjKey = parameter.getContainingClass().getName() + "$" + parameter.getParameterName() + "$" + parameter.getParameterType().getName();
        StringParse parseObj = ParseDataHandlerMethodArgumentResolver.MAPPERS.get(parseObjKey);
        if (parseObj == null) {
            parseObj = this.generateParseClassBody(dest, parseObjKey);
        }

        List result = StringUtils.convert(valueInRequest, delimiters, parseObj);

        if (CollectionUtils.isEmpty(result) && parse.required()) {
            throw new RuntimeException("参数[" + parameter.getParameterName() + "]是必填的！");
        }

        return result;
    }

    private Class<?> getClassGenricType(MethodParameter parameter) {
        Type type = parameter.getGenericParameterType();
        if (!(type instanceof ParameterizedType)) {
            return Object.class;
        }

        Type[] types = ((ParameterizedType) parameter.getGenericParameterType()).getActualTypeArguments();
        if (types == null || types.length == 0) {
            return Object.class;
        }

        return (Class<?>) types[0];
    }

    private StringParse generateParseClassBody(Class<?> dest, String parseObjKey) {
        try {
            ClassPool cp = CLASSPOOL;
            //创建一个类
            CtClass ctClass = cp.makeClass("lodsve.core.utils.StringParse$" + System.currentTimeMillis());
            //创建一个接口(StringUtils.StringParse)
            CtClass ctInterface = cp.makeInterface("lodsve.core.utils.StringParse");
            //上面创建的类实现上面的接口
            ctClass.addInterface(ctInterface);

            //创建实现RowMapper接口的mapRow方法
            CtMethod parseMethod = CtMethod.make("public Object parse(String str) {" +
                    "	return null;" +
                    "}", ctClass);
            //方法体
            StringBuilder body = new StringBuilder();
            body.append("{\n");
            String destClass = "";
            if (Integer.class.equals(dest)) {
                destClass = "java.lang.Integer";
            } else if (Long.class.equals(dest)) {
                destClass = "java.lang.Long";
            } else if (Double.class.equals(dest)) {
                destClass = "java.lang.Double";
            } else if (Float.class.equals(dest)) {
                destClass = "java.lang.Float";
            }

            if (StringUtils.isEmpty(destClass)) {
                body.append("return $1;\n");
            } else {
                body.append("return lodsve.core.utils.NumberUtils.isNumber($1) ? ").append(destClass).append(".valueOf($1) : null;\n");
            }

            body.append("}\n");
            parseMethod.setBody(body.toString());
            //加入这个方法
            ctClass.addMethod(parseMethod);

            StringParse obj = (StringParse) ctClass.toClass().newInstance();
            ParseDataHandlerMethodArgumentResolver.MAPPERS.put(parseObjKey, obj);

            return obj;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }
}

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

package lodsve.mybatis.repository.helper;

import lodsve.mybatis.repository.provider.BaseMapperProvider;
import lodsve.mybatis.repository.provider.EmptyMapperProvider;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.mapping.MappedStatement;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 处理主要逻辑，最关键的一个类.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 */
public class MapperHelper {
    public static final String PROVIDER_METHOD_NAME = "dynamicSQL";
    public static final String STRING_POINT = ".";

    /**
     * 注册的通用Mapper接口
     */
    private static final Map<Class<?>, BaseMapperProvider> REGISTER_MAPPER = new ConcurrentHashMap<>();

    /**
     * 缓存msid和MapperTemplate
     */
    private static final Map<String, BaseMapperProvider> MS_ID_CACHE = new HashMap<>();
    /**
     * 缓存skip结果
     */
    private static final Map<String, Boolean> MS_ID_SKIP = new HashMap<>();

    /**
     * 默认构造方法
     */
    private MapperHelper() {
    }

    /**
     * 判断当前的接口方法是否需要进行拦截
     *
     * @param msId msId
     * @return true/false
     */
    public static boolean isMapperMethod(String msId) {
        if (MS_ID_SKIP.get(msId) != null) {
            return MS_ID_SKIP.get(msId);
        }
        for (Map.Entry<Class<?>, BaseMapperProvider> entry : REGISTER_MAPPER.entrySet()) {
            if (entry.getValue().supportMethod(msId)) {
                MS_ID_SKIP.put(msId, true);
                return true;
            }
        }
        MS_ID_SKIP.put(msId, false);
        return false;
    }

    /**
     * 重新设置SqlSource
     *
     * @param ms        MappedStatement
     * @param parameter 参数
     */
    public static void resetSqlSource(MappedStatement ms, MapperMethod.ParamMap parameter) {
        BaseMapperProvider baseMapperProvider = getMapperProvider(ms.getId());
        try {
            if (baseMapperProvider != null) {
                baseMapperProvider.resetSqlSource(ms, parameter);
            }
        } catch (Exception e) {
            throw new RuntimeException("调用方法异常:" + e.getMessage());
        }
    }

    /**
     * 获取MapperTemplate
     *
     * @param msId msId
     * @return MapperTemplate
     */
    public static BaseMapperProvider getMapperProvider(String msId) {
        BaseMapperProvider baseMapperProvider = null;
        if (MS_ID_CACHE.get(msId) != null) {
            baseMapperProvider = MS_ID_CACHE.get(msId);
        } else {
            for (Map.Entry<Class<?>, BaseMapperProvider> entry : REGISTER_MAPPER.entrySet()) {
                if (entry.getValue().supportMethod(msId)) {
                    baseMapperProvider = entry.getValue();
                    break;
                }
            }
            MS_ID_CACHE.put(msId, baseMapperProvider);
        }
        return baseMapperProvider;
    }

    /**
     * 注册通用Mapper接口
     *
     * @param mapperClass mapperClass
     */
    public static void registerMapper(Class<?> mapperClass) {
        if (!REGISTER_MAPPER.containsKey(mapperClass)) {
            REGISTER_MAPPER.put(mapperClass, fromMapperProvider(mapperClass));
        }
        //自动注册继承的接口
        Class<?>[] interfaces = mapperClass.getInterfaces();
        if (interfaces != null && interfaces.length > 0) {
            for (Class<?> anInterface : interfaces) {
                registerMapper(anInterface);
            }
        }
    }

    /**
     * 通过通用Mapper接口获取对应的MapperTemplate
     *
     * @param mapperClass mapperClass
     * @return MapperTemplate
     */
    private static BaseMapperProvider fromMapperProvider(Class<?> mapperClass) {
        Method[] methods = mapperClass.getDeclaredMethods();
        Class<?> templateClass = null;
        Class<?> tempClass = null;
        Set<String> methodSet = new HashSet<>();
        for (Method method : methods) {
            methodSet.add(method.getName());
            if (method.isAnnotationPresent(SelectProvider.class)) {
                SelectProvider provider = method.getAnnotation(SelectProvider.class);
                tempClass = provider.type();
            } else if (method.isAnnotationPresent(InsertProvider.class)) {
                InsertProvider provider = method.getAnnotation(InsertProvider.class);
                tempClass = provider.type();
            } else if (method.isAnnotationPresent(DeleteProvider.class)) {
                DeleteProvider provider = method.getAnnotation(DeleteProvider.class);
                tempClass = provider.type();
            } else if (method.isAnnotationPresent(UpdateProvider.class)) {
                UpdateProvider provider = method.getAnnotation(UpdateProvider.class);
                tempClass = provider.type();
            }

            if (templateClass == null) {
                templateClass = tempClass;
            } else if (templateClass != tempClass) {
                throw new RuntimeException("一个通用Mapper中只允许存在一个MapperTemplate子类!");
            }
        }
        if (templateClass == null || !BaseMapperProvider.class.isAssignableFrom(templateClass)) {
            templateClass = EmptyMapperProvider.class;
        }
        BaseMapperProvider mapperProvider;
        try {
            mapperProvider = (BaseMapperProvider) templateClass.getConstructor(Class.class).newInstance(mapperClass);
        } catch (Exception e) {
            throw new RuntimeException("实例化MapperTemplate对象失败:" + e.getMessage());
        }
        //注册方法
        for (String methodName : methodSet) {
            Method method;
            try {
                method = templateClass.getMethod(methodName, MappedStatement.class);
            } catch (NoSuchMethodException e) {
                try {
                    method = templateClass.getMethod(methodName, MappedStatement.class, Object.class);
                } catch (NoSuchMethodException e1) {
                    throw new RuntimeException(templateClass.getCanonicalName() + "中缺少" + methodName + "方法!");
                }
            }

            mapperProvider.addMethodMap(methodName, method);
        }
        return mapperProvider;
    }
}

package lodsve.mybatis.utils;

import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.lang.reflect.Proxy;

/**
 * plugin utils.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2017/6/12 下午8:26
 */
public final class PluginUtils {
    private PluginUtils() {
    }

    /**
     * 获得真正的处理对象,可能多层代理.
     *
     * @param target 处理对象
     * @return 真正的处理对象
     */
    public static Object realTarget(Object target) {
        if (Proxy.isProxyClass(target.getClass())) {
            MetaObject metaObject = SystemMetaObject.forObject(target);
            return realTarget(metaObject.getValue("h.target"));
        }
        return target;
    }
}

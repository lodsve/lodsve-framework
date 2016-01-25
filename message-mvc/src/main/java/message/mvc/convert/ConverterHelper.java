package message.mvc.convert;

import message.base.context.ApplicationHelper;
import message.base.utils.GenericsUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 转换器的工具类.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/11/9 下午3:47
 */
public final class ConverterHelper {
    private static final Map<Class<?>, AbstractConverter> CONVERTER_STORAGE = new HashMap<>();
    private static boolean isInit = false;

    public static AbstractConverter getConverter(Class<?> domain) {
        if (!isInit) {
            Collection<AbstractConverter> converters = ApplicationHelper.getInstance().getBeansByType(AbstractConverter.class).values();
            for (AbstractConverter converter : converters) {
                Class p0 = GenericsUtils.getClassGenericType0(converter.getClass());
                CONVERTER_STORAGE.put(p0, converter);
            }
        }

        if (domain == null) {
            return null;
        }

        return CONVERTER_STORAGE.get(domain);
    }
}

package lodsve.mvc.convert;

import lodsve.core.bean.Codeable;
import lodsve.core.utils.StringUtils;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalConverter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

/**
 * spring mvc将传递的参数转换成枚举.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 2014-12-17 20:23
 */
public class EnumCodeConverterFactory implements ConverterFactory<String, Enum<?>>, ConditionalConverter {
    @Override
    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        Class<?> clazz = targetType.getType();
        return Enum.class.isAssignableFrom(clazz) && Codeable.class.isAssignableFrom(clazz);
    }

    @Override
    public <T extends Enum<?>> Converter<String, T> getConverter(Class<T> targetType) {
        if (!Codeable.class.isAssignableFrom(targetType)) {
            return null;
        }

        return new ValueToEnum(targetType);
    }

    private class ValueToEnum<T extends Enum<T> & Codeable> implements Converter<String, T> {
        private T[] enums;
        private Class<T> enumType;

        private ValueToEnum(Class<T> enumType) {
            this.enumType = enumType;
            this.enums = enumType.getEnumConstants();
        }

        @Override
        public T convert(String source) {
            if (StringUtils.isBlank(source)) {
                return null;
            }

            T result;
            try {
                result = Enum.valueOf(enumType, source);
            } catch (Exception e) {
                result = null;
            }

            if (result != null) {
                return result;
            }

            for (T em : enums) {
                if (em.getCode().equals(source)) {
                    result = em;
                    break;
                }
            }

            return result;
        }
    }
}

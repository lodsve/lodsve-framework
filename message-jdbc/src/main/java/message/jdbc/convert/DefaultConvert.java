package message.jdbc.convert;

import message.utils.StringUtils;

/**
 * 默认的枚举转换器，没有特殊情况就直接继承.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/6/24 下午10:18
 */
public abstract class DefaultConvert<T extends Enum<?> & ConvertGetter> implements Convert<T> {
    private final T[] enums;

    public DefaultConvert(Class<T> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.enums = type.getEnumConstants();
        if (this.enums == null) {
            throw new IllegalArgumentException(type.getSimpleName() + " does not represent an enum type.");
        }
    }

    @Override
    public String getDbValue(T t) {
        return t.getValue();
    }

    @Override
    public String getDbNullValue(T t) {
        return StringUtils.EMPTY;
    }

    @Override
    public T getPoJoValue(String value) {
        return convert(value);
    }

    @Override
    public T getPoJoNullValue(String value) {
        return convert(value);
    }

    private T convert(String value) {
        for (T em : enums) {
            if (em.getValue().equals(value)) {
                return em;
            }
        }
        return null;
    }
}

package lodsve.base.utils;

/**
 * 类型转换器的接口.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 2015-2-9 22:07
 */
public interface StringParse<V> {
    /**
     * 转换
     *
     * @param str 待转换的数据
     * @return
     */
    public V parse(String str);
}

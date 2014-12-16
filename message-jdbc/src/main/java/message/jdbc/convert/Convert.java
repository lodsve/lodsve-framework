package message.jdbc.convert;

/**
 * 类型转换.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 2014-12-15 21:29
 */
public interface Convert<T extends ConvertGetter> {
    /**
     * 根据pojo中的属性值获取在数据库中的值
     *
     * @param t
     * @return
     */
    public Object getDbValue(T t);

    /**
     * 根据pojo中的属性值获取在数据库中的值[为空的情况]
     *
     * @param t
     * @return
     */
    public Object getDbNullValue(T t);

    /**
     * 根据在数据库中的值包装pojo中的属性值
     *
     * @param value
     * @return
     */
    public T getPoJoValue(Object value);

    /**
     * 根据在数据库中的值包装pojo中的属性值[为空的情况]
     *
     * @param value
     * @return
     */
    public T getPoJoNullValue(Object value);
}

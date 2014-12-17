package message.jdbc.convert;

/**
 * 设计到类型转换的字段类型必须实现此接口.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 2014-12-15 22:05
 */
public interface ConvertGetter1 {
    /**
     * 获取在数据库中存储的值
     *
     * @return
     */
    public String getValue();
}

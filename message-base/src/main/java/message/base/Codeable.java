package message.base;

/**
 * 枚举-->code.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/12/29 下午2:47
 */
public interface Codeable {
    /**
     * 获取在数据库中存储的值
     *
     * @return
     */
    String getCode();

    /**
     * 枚举显示的值
     *
     * @return
     */
    String getTitle();
}

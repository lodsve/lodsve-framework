package message.base.utils;

import java.util.Map;

/**
 * 检查excel中每一行的数据是否合法.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 14-2-25 上午10:01
 */
public interface CheckExcel {
    /**
     * 返回true合法
     *
     * @param data      excel中每一行的数据
     * @return
     */
    public boolean check(Map<String, String> data);
}

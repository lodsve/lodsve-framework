package message.wechat.exception;

import message.base.utils.StringUtils;

import java.util.ResourceBundle;

/**
 * 异常的处理.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2016-1-27 13:43
 */
public class WeChatExceptionHandler {
    private static ResourceBundle bundle;

    private WeChatExceptionHandler() {
        bundle = ResourceBundle.getBundle("META-INF.wechat.error");
    }

    public static String eval(WeChatException ex) {
        return ex == null ? StringUtils.EMPTY : eval(ex.getErrCode());
    }

    public static String eval(String errCode) {
        if (StringUtils.isNotBlank(errCode) && bundle != null) {
            return bundle.getString(errCode);
        }

        return StringUtils.EMPTY;
    }
}

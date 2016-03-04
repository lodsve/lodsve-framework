package message.wechat.base;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * 语言枚举.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/3/2 下午2:10
 */
public enum Lang {
    /**
     * zh_CN 简体，zh_TW 繁体，en 英语
     */
    zh_CN, zh_TW, en;

    @JsonCreator
    public static Lang eval(String v) {
        switch (v) {
            case "zh_CN":
                return zh_CN;
            case "zh_TW":
                return zh_TW;
            case "en":
                return en;
            default:
                break;
        }
        return null;
    }
}

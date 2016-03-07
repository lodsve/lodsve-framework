package message.wechat.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * 用户性别枚举.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/3/2 下午4:27
 */
public enum Sex {
    MAN, WOMAN, UNKNOWN;

    @JsonCreator
    public static Sex eval(int index) {
        switch (index) {
            case 1:
                return MAN;
            case 2:
                return WOMAN;
            case 0:
                return UNKNOWN;
            default:
                break;
        }

        return null;
    }
}

package message.wechat.beans;

import message.wechat.enums.Lang;

/**
 * 用户查询参数封装.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/3/7 下午10:48
 */
public class UserQuery {
    /**
     * 用户的标识，对当前公众号唯一
     */
    private String openId;
    /**
     * 用户的语言，简体中文为zh_CN
     */
    private Lang language;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Lang getLanguage() {
        return language;
    }

    public void setLanguage(Lang language) {
        this.language = language;
    }
}

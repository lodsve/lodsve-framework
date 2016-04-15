package message.workflow.enums;

import message.base.bean.Codeable;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/4/15 下午2:43
 */
public enum UrlType implements Codeable {
    VIEW("100", "查看URL"), UPDATE("101", "修改URL");

    private String code;
    private String title;

    UrlType(String code, String title) {
        this.code = code;
        this.title = title;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getTitle() {
        return title;
    }
}

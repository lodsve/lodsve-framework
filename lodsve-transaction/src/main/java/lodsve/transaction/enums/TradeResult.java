package lodsve.transaction.enums;

import lodsve.core.bean.Codeable;

/**
 * 支付状态.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/8/17 下午4:27
 */
public enum TradeResult implements Codeable {
    YES("101", "成功"), NO("102", "未进行"), PROCESSING("103", "中"), ERROR("104", "失败");

    private String code;
    private String title;

    TradeResult(String code, String title) {
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
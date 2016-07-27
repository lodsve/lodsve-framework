package lodsve.transaction.enums;

import lodsve.core.bean.Codeable;

/**
 * 消费类型.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/8/19 下午2:28
 */
public enum TradeType implements Codeable {
    TRADE_CONSUME("101", "消费"), TRADE_RETURN("102", "退货");

    private String code;
    private String title;

    TradeType(String code, String title) {
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

package message.transaction.channel;

import message.config.SystemConfig;
import message.transaction.PayException;
import message.transaction.enums.TradeChannel;
import message.transaction.enums.TradeType;
import message.transaction.utils.Channel;
import message.transaction.utils.PingConfig;
import message.utils.ParamsHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 支付宝手机网页支付.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/9/25 下午3:07
 */
@Component
@Channel(TradeChannel.ALIPAY_WAP)
public class AliPayWap extends CommonAliPay {
    @Autowired
    private PingConfig pingConfig;
    @Autowired
    private SystemConfig systemConfig;

    @Override
    protected Map<String, String> buildExtraData() {
        Map<String, String> params = new HashMap<>();

        Map<String, PingConfig.AliPayWapNotify> notifyMap = this.pingConfig.getNotifySettings();
        TradeType type = ParamsHolder.get("tradeType");

        if (type == null) {
            throw new PayException(10016, "TradeType IS NULL!");
        }

//        TODO
//        String notifyUrl = systemConfig.getFrontEndUrl() + pingConfig.getNotifyUrl() + "/%s/" + notifyMap.get(type.toString()).getFrom();
//        params.put("success_url", String.format(notifyUrl, "success"));
//        params.put("cancel_url", String.format(notifyUrl, "cancel"));

        return params;
    }
}

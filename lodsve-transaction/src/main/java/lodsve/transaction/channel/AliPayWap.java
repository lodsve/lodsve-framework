package lodsve.transaction.channel;

import lodsve.core.utils.ParamsHolder;
import lodsve.transaction.enums.TradeChannel;
import lodsve.transaction.enums.TradeType;
import lodsve.transaction.exception.PayException;
import lodsve.transaction.utils.Channel;
import lodsve.transaction.utils.PingppProperties;
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
    private PingppProperties pingppProperties;

    @Override
    protected Map<String, String> buildExtraData(Map<String, String> extra) {
        Map<String, String> params = new HashMap<>();

        TradeType type = ParamsHolder.get("tradeType");

        if (type == null) {
            throw new PayException(106002, "TradeType IS NULL!");
        }

        String notifyUrl = pingppProperties.getNotifyUrl() + "/%s/" + type.toString();
        params.put("success_url", String.format(notifyUrl, "success"));
        params.put("cancel_url", String.format(notifyUrl, "cancel"));

        return params;
    }
}

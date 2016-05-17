package lodsve.transaction.channel;

import lodsve.transaction.enums.TradeChannel;
import lodsve.transaction.utils.Channel;
import lodsve.transaction.utils.data.PayData;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

/**
 * 微信支付.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/10/8 上午11:20
 */
@Component
@Channel(TradeChannel.WX)
public class WeiXinPay extends CommonWXPay {
    @Override
    protected Map<String, String> buildExtraData(Map<String, String> extra) {
        if (MapUtils.isEmpty(extra)) {
            return Collections.emptyMap();
        }

        String limitPay = extra.get(PayData.LIMIT_PAY);
        Boolean limit = Boolean.valueOf(limitPay);
        if (limit) {
            return Collections.singletonMap(PayData.LIMIT_PAY, "no_credit");
        }

        return Collections.emptyMap();
    }
}

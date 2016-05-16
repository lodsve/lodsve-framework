package lodsve.transaction.channel;

import lodsve.base.utils.StringUtils;
import lodsve.transaction.enums.TradeChannel;
import lodsve.transaction.utils.Channel;
import lodsve.transaction.utils.data.PayData;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信公众号支付.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/8/28 上午11:21
 */
@Component
@Channel(TradeChannel.WX_PUB)
public class WeiXinPubPay extends CommonWXPay {
    private static final Logger logger = LoggerFactory.getLogger(WeiXinPubPay.class);

    @Override
    protected Map<String, String> buildExtraData(Map<String, String> extra) {
        if (MapUtils.isEmpty(extra)) {
            logger.error("openId必填!");
            throw new RuntimeException("openId必填!");
        }

        String openId = extra.get(PayData.OPEN_ID);
        if (StringUtils.isBlank(openId)) {
            logger.error("openId必填!");
            throw new RuntimeException("openId必填!");
        }

        Map<String, String> result = new HashMap<>();
        result.put(PayData.OPEN_ID, openId);

        String limitPay = extra.get(PayData.LIMIT_PAY);
        Boolean limit = Boolean.valueOf(limitPay);
        if (limit) {
            result.put(PayData.LIMIT_PAY, "no_credit");
        }

        return result;
    }
}

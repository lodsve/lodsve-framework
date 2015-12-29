package message.transaction.utils;

import message.transaction.action.PayAction;
import message.transaction.action.RefundAction;
import message.transaction.channel.Pay;
import message.transaction.enums.TradeChannel;
import message.transaction.enums.TradeType;
import message.utils.ListUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 交易路由.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/9/21 下午1:32
 */
@Component
public class TradeRouting implements ApplicationContextAware {
    private final static Map<TradeType, PayAction> PAYACTION_MAP = new HashMap<>();
    private final static Map<TradeChannel, Pay> PAY_MAP = new HashMap<>();
    private final static Map<TradeType, RefundAction> REFUNDACTION_MAP = new HashMap<>();

    /**
     * 根据消费类型获取支付的动作
     *
     * @param tradeType 消费类型
     * @return
     */
    public PayAction channel(TradeType tradeType) {
        if (tradeType == null) {
            return null;
        }

        return PAYACTION_MAP.get(tradeType);
    }

    /**
     * 根据支付渠道获取支付的动作
     *
     * @param channel 支付渠道
     * @return
     */
    public Pay pay(TradeChannel channel) {
        if (channel == null) {
            return null;
        }

        return PAY_MAP.get(channel);
    }

    /**
     * 根据消费类型获取退款的动作
     *
     * @param tradeType 消费类型
     * @return
     */
    public RefundAction refund(TradeType tradeType) {
        if (tradeType == null) {
            return null;
        }

        return REFUNDACTION_MAP.get(tradeType);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Collection<PayAction> payActions = applicationContext.getBeansOfType(PayAction.class).values();
        Collection<Pay> pays = applicationContext.getBeansOfType(Pay.class).values();
        Collection<RefundAction> refundActions = applicationContext.getBeansOfType(RefundAction.class).values();

        PAYACTION_MAP.putAll(ListUtils.toMap(payActions, new ListUtils.KeyFinder<PayAction, TradeType>() {
            @Override
            public TradeType findKey(PayAction target) {
                Class<?> clazz = target.getClass();
                if (!clazz.isAnnotationPresent(Trade.class)) {
                    return null;
                }

                Trade trade = clazz.getAnnotation(Trade.class);

                return trade.value();
            }
        }));

        PAY_MAP.putAll(ListUtils.toMap(pays, new ListUtils.KeyFinder<Pay, TradeChannel>() {
            @Override
            public TradeChannel findKey(Pay target) {
                Class<?> clazz = target.getClass();
                if (!clazz.isAnnotationPresent(Channel.class)) {
                    return null;
                }

                Channel _channel = clazz.getAnnotation(Channel.class);

                return _channel.value();
            }
        }));

        REFUNDACTION_MAP.putAll(ListUtils.toMap(refundActions, new ListUtils.KeyFinder<RefundAction, TradeType>() {
            @Override
            public TradeType findKey(RefundAction target) {
                Class<?> clazz = target.getClass();
                if (!clazz.isAnnotationPresent(Trade.class)) {
                    return null;
                }

                Trade trade = clazz.getAnnotation(Trade.class);

                return trade.value();
            }
        }));
    }
}

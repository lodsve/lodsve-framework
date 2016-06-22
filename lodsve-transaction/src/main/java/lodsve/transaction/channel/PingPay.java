package lodsve.transaction.channel;

import com.pingplusplus.Pingpp;
import com.pingplusplus.model.Charge;
import com.pingplusplus.model.Refund;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import lodsve.core.utils.StringUtils;
import lodsve.transaction.domain.Payment;
import lodsve.transaction.exception.RefundException;
import lodsve.transaction.utils.PingConfig;
import lodsve.transaction.utils.data.PayData;
import lodsve.transaction.utils.data.RefundData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * ping++支付实现.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/8/26 上午3:57
 */
@Component
public abstract class PingPay implements Pay<Charge, Refund> {
    @Autowired
    private PingConfig pingConfig;

    @PostConstruct
    public void init() {
        Pingpp.apiKey = this.pingConfig.getApiKey();
    }

    @Override
    public void validatePay(Long amount, Map<String, Object> extraParam) {
        // 通过ping++支付 不需要验证
    }

    @Override
    public Charge pay(PayData payData) throws Exception {
        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("order_no", asString(payData.getPaymentId()));
        chargeParams.put("amount", asString(payData.getAmount()));

        Map<String, String> app = new HashMap<>();
        app.put("id", this.pingConfig.getAppId());
        chargeParams.put("app", app);
        chargeParams.put("channel", payData.getTradeChannel().name().toLowerCase());
        chargeParams.put("currency", "cny");
        chargeParams.put("client_ip", "127.0.0.1");
        if (StringUtils.isNotBlank(payData.getProductName())) {
            chargeParams.put("subject", StringUtils.subStringWithByte(payData.getProductName(), 32));
        }
        chargeParams.put("body", payData.getPayDesc());

        // 设置额外参数
        setExtraData(chargeParams, payData.getExtra());

        return Charge.create(chargeParams);
    }

    @Override
    public Refund refund(RefundData refundData) throws Exception {
        Assert.notNull(refundData);

        Payment payment = refundData.getPayment();
        Assert.notNull(payment);

        if (StringUtils.isEmpty(payment.getChargeId())) {
            throw new RefundException(106004, "has no chargeId, payment id is " + payment.getId() + ", target id is " + payment.getTargetId());
        }

        Charge ch = Charge.retrieve(payment.getChargeId());

        if (ch == null) {
            throw new RefundException(106004, "has no charge, payment id is " + payment.getId() + ", target id is " + payment.getTargetId() +
                    ", charge id is " + payment.getChargeId());
        }

        Map<String, Object> refundMap = new HashMap<>();
        refundMap.put("amount", payment.getAmount());
        refundMap.put("description", "退款:" + payment.getDescription());
        return ch.getRefunds().create(refundMap);
    }

    private String asString(Object object) {
        if (object == null) {
            return "";
        }

        return object.toString();
    }

    private void setExtraData(Map<String, Object> chargeParams, Map<String, String> extra) {
        Map<String, String> _extra = buildExtraData(extra);
        if (extra != null && !extra.isEmpty()) {
            chargeParams.put("extra", _extra);
        }
    }

    /**
     * 设置各种支付的额外参数,以保证各种支付实现的差异性<br/>
     * 特定渠道发起交易时需要的额外参数以及部分渠道支付成功返回的额外参数。<br/>
     * <ul>
     * <li><strong>alipay:(支付宝手机支付)。</strong><br/>
     * <ol>
     * <li>支付完成将额外返回付款用户的支付宝账号 buyer_account 。</li>
     * </ol>
     * </li>
     * <li><strong>alipay_wap:(支付宝手机网页支付)。</strong><br/>
     * <ol>
     * <li>参数 success_url[string] 为支付成功的回调地址，required；</li>
     * <li>参数 cancel_url[string] 为支付取消的回调地址，optional；</li>
     * <li>支付完成将额外返回付款用户的支付宝账号 buyer_account 。</li>
     * </ol>
     * </li>
     * <li><strong>wx:(微信支付)。</strong><br/>
     * <ol>
     * <li>支付完成将额外返回付款用户的 open_id 。</li>
     * </ol>
     * </li>
     * <li><strong>wx_pub:(微信公众账号支付)。</strong><br/>
     * <ol>
     * <li>参数 open_id[string] 为用户在商户 appid 下的唯一标识，required；</li>
     * </ol>
     * </li>
     * <li><strong>upacp_wap:(银联全渠道手机网页支付)。</strong><br/>
     * <ol>
     * <li>参数 result_url[string] 为支付完成的回调地址，required；</li>
     * </ol>
     * </li>
     * <li><strong>upmp_wap:(银联手机网页支付)。</strong><br/>
     * <ol>
     * <li>参数 result_url[string] 为支付完成的回调地址，required；</li>
     * </ol>
     * </li>
     * <li><strong>bfb_wap:(百度钱包手机网页支付)。</strong><br/>
     * <ol>
     * <li>参数 result_url[string] 为支付完成的回调地址，required；</li>
     * <li>参数 bfb_login[boolean] 为是否需要登录百度钱包来进行支付，required；</li>
     * </ol>
     * </li>
     * <li><strong>apple_pay:</strong><br/>
     * <ol>
     * <li>参数 payment_token[string] 为支付所需的支付令牌，从 client 获得，required；</li>
     * </ol>
     * </li>
     * <li><strong>wx_pub_qr:(微信公众账号扫码支付)。</strong><br/>
     * <ol>
     * <li>参数 product_id[string] 为商品 ID，1-32 位字符串。此 id 为二维码中包含的商品 ID，商户自行维护，required；</li>
     * <li>支付完成将额外返回付款用户的 open_id 。</li>
     * </ol>
     * </li>
     * <li><strong>yeepay_wap:(易宝手机网页支付)。</strong><br/>
     * <ol>
     * <li>参数 product_category[string] 为商品类别码，详见商品类型码表，required；</li>
     * <li>参数 identity_id[string] 为用户标识,商户生成的用户账号唯一标识，最长 50 位字符串，required；</li>
     * <li>参数 identity_type[int] 为用户标识类型，详见用户标识类型码表，required；</li>
     * <li>参数 terminal_type[int] 为终端类型，对应取值 0:IMEI, 1:MAC, 2:UUID, 3:other，required；</li>
     * <li>参数 terminal_id[string] 为终端 ID，required；</li>
     * <li>参数 user_ua[string] 为用户使用的移动终端的 UserAgent 信息，required；</li>
     * <li>参数 result_url[string] 为前台通知地址，required。</li>
     * </ol>
     * </li>
     * <li><strong>jdpay_wap:(京东手机网页支付)。</strong><br/>
     * <ol>
     * <li>参数 success_url[string] 为支付成功页面跳转路径，required；</li>
     * <li>参数 fail_url[string] 为支付失败页面跳转路径，required；</li>
     * <li>参数 token[string] 为用户交易令牌，用于识别用户信息，支付成功后会调用 success_url 返回给商户。商户可以记录这个 token 值，当用户再次支付的时候传入该 token，用户无需再次输入银行卡信息，直接输入短信验证码进行支付。32 位字符串，optional；</li>
     * </ol>
     * </li>
     * </ul>
     *
     * @param extra 一些额外的参数
     * @return
     */
    protected abstract Map<String, String> buildExtraData(Map<String, String> extra);
}

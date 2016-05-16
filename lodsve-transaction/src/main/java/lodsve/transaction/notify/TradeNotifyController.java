package lodsve.transaction.notify;

import com.pingplusplus.model.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ping++操作后回调.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/8/26 上午7:45
 */
@Controller
@RequestMapping("/pingpp")
public class TradeNotifyController {
    private static final Logger logger = LoggerFactory.getLogger(TradeNotifyController.class);

    /**
     * 交易成功返回字符
     */
    private static final String SUCCESS = "charge.succeeded";
    private static final String EVENT_ORDER_NO = "order_no";
    private static final String EVENT_AMOUNT = "amount";
    private static final String EVENT_BUYER_ACCOUNT = "buyer_account";
    private static final String EVENT_CHARGE_ID = "charge";

    @Autowired
    private TradeNotifyService tradeNotifyService;

    /**
     * 支付成功后回调
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/afterPay")
    public void afterPay(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.info("into method afterPay");
        //解析Ping++异步通知数据
        Event event = WebhooksUtils.parsePingEvent(request);
        Assert.notNull(event, "ping++支付通知对象不能为空");

        // 支付单ID
        String paymentId = WebhooksUtils.findDataFromEventData(event, EVENT_ORDER_NO);
        Double amount = WebhooksUtils.findDataFromEventData(event, EVENT_AMOUNT);
        String account = WebhooksUtils.findDataFromEventData(event, EVENT_BUYER_ACCOUNT);
        String type = event.getType();

        boolean result = this.tradeNotifyService.payCallback(paymentId, amount, account, SUCCESS.equals(type));

        pingppResponse(response, result);
    }

    /**
     * 退款后回调
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/afterRefund")
    public void afterRefund(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.info("into method afterRefund");
        //解析Ping++异步通知数据
        Event event = WebhooksUtils.parsePingEvent(request);
        Assert.notNull(event, "ping++支付通知对象不能为空");

        String chargeId = WebhooksUtils.findDataFromEventData(event, EVENT_CHARGE_ID);
        Double amount = WebhooksUtils.findDataFromEventData(event, EVENT_AMOUNT);
        String type = event.getType();

        boolean result = this.tradeNotifyService.refundCallback(chargeId, amount, SUCCESS.equals(type));

        pingppResponse(response, result);
    }

    private void pingppResponse(HttpServletResponse response, boolean result) {
        if (result)
            response.setStatus(200);
        else
            response.setStatus(500);
    }
}

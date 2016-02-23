package message.wechat.web;

import message.base.utils.StringUtils;
import message.base.utils.XmlUtils;
import message.mvc.annotation.WebResource;
import message.mvc.commons.WebInput;
import message.wechat.beans.JsApiConfig;
import message.wechat.beans.ValidateSignature;
import message.wechat.beans.message.handler.ReceiveHandler;
import message.wechat.beans.message.reply.Reply;
import message.wechat.base.WeChatCommonsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/21 下午5:13
 */
@RestController
@RequestMapping("/wx")
public class WeChatCommonsController {
    @Autowired
    private WeChatCommonsService weChatCommonsService;
    @Autowired
    private ReceiveHandler receiveHandler;

    @RequestMapping(value = "/message", produces = "application/json", method = RequestMethod.GET)
    public String check(@RequestBody ValidateSignature signature) {
        Assert.notNull(signature);

        boolean result = weChatCommonsService.checkSignature(signature.getTimestamp(), signature.getNonce(), signature.getSignature());
        if (result) {
            return signature.getEchostr();
        } else {
            return StringUtils.EMPTY;
        }
    }

    @RequestMapping(value = "/message", produces = "application/json", method = RequestMethod.POST)
    public String handleMessage(@WebResource WebInput in) {
        try {
            Reply reply = receiveHandler.handle(XmlUtils.parse(in.getRequest().getInputStream()));
            return XmlUtils.toXML(reply);
        } catch (Exception e) {
            return StringUtils.EMPTY;
        }
    }

    @RequestMapping(value = "/js/config", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public JsApiConfig jsApiConfig(String url) {
        return weChatCommonsService.jsApiConfig(url);
    }
}

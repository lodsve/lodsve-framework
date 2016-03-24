package message.wechat.web;

import java.io.IOException;
import java.io.PrintWriter;
import message.base.utils.StringUtils;
import message.base.utils.XmlUtils;
import message.mvc.annotation.WebResource;
import message.mvc.commons.WebInput;
import message.mvc.commons.WebOutput;
import message.wechat.api.base.WeChatService;
import message.wechat.api.message.WeChatReceiveHandler;
import message.wechat.beans.JsApiConfig;
import message.wechat.beans.message.reply.Reply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 微信配置校验及消息/事件捕获.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/21 下午5:13
 */
@ApiIgnore
@RestController
@RequestMapping("/wx")
public class WeChatController {
    @Autowired
    private WeChatService weChatService;
    @Autowired
    private WeChatReceiveHandler weChatReceiveHandler;

    /**
     * 校验配置
     *
     * @param signature 签名
     * @param timestamp 时间戳
     * @param nonce     随机数
     * @param echostr   随机字符串
     * @param out       response
     * @return
     * @throws IOException
     */
    @ApiIgnore
    @RequestMapping(value = "/message", produces = "application/json", method = RequestMethod.GET)
    public String check(String signature, String timestamp, String nonce, String echostr, @WebResource WebOutput out) throws IOException {
        Assert.notNull(signature);
        Assert.notNull(timestamp);
        Assert.notNull(nonce);
        Assert.notNull(echostr);

        boolean result = weChatService.checkSignature(timestamp, nonce, signature);
        PrintWriter writer = out.getResponse().getWriter();
        if (result) {
            writer.print(echostr);
        } else {
            writer.print(StringUtils.EMPTY);
        }

        writer.flush();
        writer.close();

        return StringUtils.EMPTY;
    }

    /**
     * 接受消息/事件处理
     *
     * @param in request
     * @return
     */
    @ApiIgnore
    @RequestMapping(value = "/message", produces = "application/json", method = RequestMethod.POST)
    public String handleMessage(@WebResource WebInput in) {
        try {
            Reply reply = weChatReceiveHandler.handle(XmlUtils.parse(in.getRequest().getInputStream()));
            return XmlUtils.toXML(reply);
        } catch (Exception e) {
            return StringUtils.EMPTY;
        }
    }

    /**
     * 获取JS-API配置
     *
     * @param url
     * @return
     */
    @ApiIgnore
    @RequestMapping(value = "/js/config", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public JsApiConfig jsApiConfig(String url) {
        return weChatService.jsApiConfig(url);
    }
}

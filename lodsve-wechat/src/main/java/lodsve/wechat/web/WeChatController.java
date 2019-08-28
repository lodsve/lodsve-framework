/*
 * Copyright (C) 2019 Sun.Hao(https://www.crazy-coder.cn/)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package lodsve.wechat.web;

import lodsve.core.utils.StringUtils;
import lodsve.core.utils.XmlUtils;
import lodsve.web.mvc.commons.WebInput;
import lodsve.web.mvc.commons.WebOutput;
import lodsve.wechat.api.base.WeChatService;
import lodsve.wechat.api.message.WeChatReceiveHandler;
import lodsve.wechat.beans.JsApiConfig;
import lodsve.wechat.beans.message.reply.Reply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * 微信配置校验及消息/事件捕获.
 *
 * @author sunhao(sunhao.java @ gmail.com)
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
    public String check(String signature, String timestamp, String nonce, String echostr, WebOutput out) throws IOException {
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
    public String handleMessage(WebInput in) {
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

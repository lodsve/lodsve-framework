package message.wechat.web;

import com.mangofactory.swagger.annotations.ApiIgnore;
import message.base.bean.ClientType;
import message.base.utils.RequestUtils;
import message.swagger.annotations.SwaggerIgnore;
import message.wechat.oauth.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/23 下午2:12
 */
@SwaggerIgnore
@RestController
@RequestMapping("/wx")
public class WeChatOAuthController {
    @Autowired
    private OAuthService oAuthService;

    @ApiIgnore
    @RequestMapping("/init")
    public String init(String url) {
        if (RequestUtils.getClientType() == ClientType.WEIXIN) {
            String redirect = oAuthService.getOAuthUrl(url);
            return "redirect:" + redirect;
        }

        return "redirect:" + url;
    }

    @ApiIgnore
    @RequestMapping("/openId")
    public String openId(String url, String code) {
        return "redirect:" + url + "?openId=" + oAuthService.getOAuthToken(code).getOpenId();
    }
}

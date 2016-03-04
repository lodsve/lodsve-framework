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
 * 微信oauth认证.
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

    /**
     * 跳转
     *
     * @param url 前台url
     * @return
     */
    @ApiIgnore
    @RequestMapping("/init")
    public String init(String url) {
        if (RequestUtils.getClientType() == ClientType.WEIXIN) {
            String redirect = oAuthService.getOAuthUrl(url);
            return "redirect:" + redirect;
        }

        return "redirect:" + url;
    }

    /**
     * 获取openId
     *
     * @param url
     * @param code
     * @return
     */
    @ApiIgnore
    @RequestMapping("/openId")
    public String openId(String url, String code) {
        return "redirect:" + url + "?openId=" + oAuthService.getOAuthToken(code).getOpenId();
    }
}

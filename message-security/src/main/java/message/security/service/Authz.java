package message.security.service;

import message.security.SecurityConstants;
import message.security.pojo.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 鉴权.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 2014-12-7 14:00
 */
@Component
public class Authz {
    private static final Logger logger = LoggerFactory.getLogger(Authz.class);

    @Autowired
    private AccountService accountService;

    /**
     * 判断是否登录
     *
     * @param request
     * @return
     */
    public boolean isLogin(HttpServletRequest request) {
        Assert.isNull(request, "HttpServletRequest不能为空！");

        return getLoginAccount(request) != null;
    }

    /**
     * 获取登录的账号
     *
     * @param request
     * @return
     */
    public Account getLoginAccount(HttpServletRequest request) {
        Assert.isNull(request, "HttpServletRequest不能为空！");

        Object obj = request.getSession().getAttribute(SecurityConstants.ACCOUNT_KEY_IN_SESSION);
        if (obj == null) {
            logger.warn("not login!");
            return null;
        }

        try {
            Account account = (Account) obj;
            return account;
        } catch (Exception e) {
            logger.warn("not login! cast error!");
            return null;
        }
    }

    /**
     * 鉴权，判断指定用户是否在给定的角色中
     *
     * @param loginName 指定用户
     * @param codes     给定的角色
     * @return
     */
    public boolean authz(String loginName, String[] codes) {
        Assert.hasText(loginName, "用户名必填！");
        Assert.noNullElements(codes, "角色code集合必填！");

        List<String> roleCodes = this.accountService.loadRoleByAccount(loginName);

        for (String code : codes) {
            if (roleCodes.contains(code)) {
                return true;
            }
        }

        return false;
    }
}

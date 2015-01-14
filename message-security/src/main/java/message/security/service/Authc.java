package message.security.service;

import message.security.SecurityConstants;
import message.security.exception.SecurityException;
import message.security.pojo.Account;
import message.security.pojo.Role;
import message.utils.EncryptUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;

/**
 * 提供用户注册、修改等功能.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 2014-12-5 20:20
 */
@Component
public class Authc {
    @Autowired
    private AccountService accountService;

    /**
     * 注册用户
     *
     * @param loginName 登录名
     * @param password  密码(明文)
     * @param roles     所属角色的code
     * @return
     */
    public Account register(String loginName, String password, String[] roles) {
        Assert.hasText(loginName, "用户名必填！");
        Assert.hasText(password, "密码必填！");

        Account account = this.accountService.loadAccount(loginName);
        if (account != null) {
            throw new SecurityException(SecurityConstants.SECUTIRY_EXCEPTION_CODE, "用户名为'{0}'的用户已存在！", loginName);
        }

        try {
            account = this.accountService.save(new Account(loginName, password));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (roles != null && roles.length > 0) {
            //保存关系
            this.accountService.saveAccountRole(roles, loginName);
        }

        return account;
    }

    /**
     * 登录校验
     *
     * @param request
     * @param loginName 登录名
     * @param password  密码(明文)
     * @return
     */
    public boolean login(HttpServletRequest request, String loginName, String password) {
        Assert.hasText(loginName, "用户名必填！");
        Assert.hasText(password, "密码必填！");

        Account account = this.accountService.loadAccount(loginName);
        if (account == null) {
            return false;
        }

        if (EncryptUtils.encodeMD5(password).equals(account.getPassword())) {
            this.putInSession(request, account);
            return true;
        }

        return false;
    }

    private void putInSession(HttpServletRequest request, Account account) {
        request.getSession().setAttribute(SecurityConstants.ACCOUNT_KEY_IN_SESSION, account);
    }

    /**
     * 修改密码
     *
     * @param loginName   登录名
     * @param oldPassword 愿密码(明文)
     * @param password    新密码(明文)
     * @return
     */
    public boolean chgPwd(String loginName, String oldPassword, String password) {
        Assert.hasText(loginName, "用户名必填！");
        Assert.hasText(oldPassword, "愿密码必填！");
        Assert.hasText(password, "密码必填！");

        Account account = this.accountService.loadAccount(loginName);
        if (account == null) {
            return false;
        }

        if (!EncryptUtils.encodeMD5(password).equals(account.getPassword())) {
            return false;
        }

        return this.accountService.chgPwd(loginName, password);
    }

    /**
     * 修改账户的角色
     *
     * @param loginName 账户
     * @param roleCodes 新的角色集合
     */
    public void chgAccountRole(String loginName, String[] roleCodes) {
        Assert.hasText(loginName, "用户名必填！");

        if (roleCodes != null && roleCodes.length > 0) {
            //保存关系
            this.accountService.saveAccountRole(roleCodes, loginName);
        }
    }

    /**
     * 保存角色
     *
     * @param roleCode 角色code
     * @return
     */
    public Role saveRole(String roleCode) {
        Assert.hasText(roleCode, "角色code必填！");

        Role role = this.accountService.loadRole(roleCode);
        if (role == null) {
            throw new SecurityException(SecurityConstants.SECUTIRY_EXCEPTION_CODE, "code为'{0}'的角色已存在！", roleCode);
        }

        try {
            role = this.accountService.saveRole(new Role(roleCode));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return role;
    }
}

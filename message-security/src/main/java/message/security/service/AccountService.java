package message.security.service;

import message.security.pojo.Account;
import message.security.pojo.AccountRole;
import message.security.pojo.Role;
import message.security.repository.AccountRepository;
import message.security.repository.AccountRoleRepository;
import message.security.repository.RoleRepository;
import message.base.utils.EncryptUtils;
import message.validate.core.NeedValidate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 提供修改密码等功能.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 2014-12-5 20:12
 */
@Service
public class AccountService {
    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountRoleRepository accountRoleRepository;
    @Autowired
    private RoleRepository roleRepository;

    /**
     * 保存用户
     *
     * @param account 账号
     * @return
     * @throws Exception
     */
    @NeedValidate
    protected Account save(Account account) throws Exception {
        account.setPassword(EncryptUtils.encodeMD5(account.getPassword()));
        account.setCreateDate(new Date());

        int i = this.accountRepository.insert(account);
        if (i == 1) {
            return account;
        } else {
            return null;
        }
    }


    protected Account loadAccount(String loginName) {
        return this.accountRepository.loadAccount(loginName);
    }

    /**
     * 修改密码
     *
     * @param loginName 登录名
     * @param password  新密码(明文)
     * @return
     */
    protected void chgPwd(String loginName, String password) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("loginName", loginName);
        params.put("password", EncryptUtils.encodeMD5(password));

        this.accountRepository.chgPwd(loginName, EncryptUtils.encodeMD5(password));
    }

    /**
     * 保存角色
     *
     * @param role 角色
     * @return
     * @throws Exception
     */
    @NeedValidate
    protected Role saveRole(Role role) throws Exception {
        role.setCreateTime(new Date());

        int i = this.roleRepository.insert(role);
        if (i == 1) {
            return role;
        } else {
            return null;
        }
    }

    /**
     * 保存角色
     *
     * @param roleCode 角色code
     * @return
     */
    protected Role loadRole(String roleCode) {
        return this.roleRepository.loadRole(roleCode);
    }

    /**
     * 保存账户角色关系
     *
     * @param roleCodes 角色code
     * @param account   账户
     */
    protected void saveAccountRole(String[] roleCodes, String account) {
        String sql;
        //1.先删除原来的账户角色关系
        accountRoleRepository.deleteAccountRoles(account);
        //2.保存现有的
        for (String code : roleCodes) {
            AccountRole accountRole = new AccountRole(account, code);
            try {
                this.accountRoleRepository.insert(accountRole);
            } catch (Exception e) {
                logger.error("保存账户角色关系出错，关系：" + accountRole, e);
                continue;
            }
        }
    }

    /**
     * 获取指定账户的角色code
     *
     * @param loginName 指定账户
     * @return
     */
    protected List<String> loadRoleByAccount(String loginName) {
        return this.accountRoleRepository.loadRoleByAccount(loginName);
    }
}

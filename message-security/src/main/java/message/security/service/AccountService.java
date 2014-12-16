package message.security.service;

import message.jdbc.GenericJdbcDAO;
import message.security.SecurityConstants;
import message.security.pojo.Account;
import message.security.pojo.AccountRole;
import message.security.pojo.Role;
import message.utils.MD5Utils;
import message.validate.core.NeedValidate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collections;
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
    @Qualifier("securityGenericJdbcDAO")
    private GenericJdbcDAO genericJdbcDAO;

    /**
     * 保存用户
     *
     * @param account 账号
     * @return
     * @throws Exception
     */
    @NeedValidate
    protected Account save(Account account) throws Exception {
        account.setPassword(MD5Utils.MD5Encode(account.getPassword()));
        account.setCreateDate(new Date());

        return this.genericJdbcDAO.genericInsert(account);
    }


    protected Account loadAccount(String loginName) {
        String sql = "select " + SecurityConstants.T_ACCOUNT + " t where t.login_name = :loginName";

        return this.genericJdbcDAO.queryForBean(sql, Collections.singletonMap("loginName", loginName), Account.class);
    }

    /**
     * 修改密码
     *
     * @param loginName 登录名
     * @param password  新密码(明文)
     * @return
     */
    protected boolean chgPwd(String loginName, String password) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("loginName", loginName);
        params.put("password", password);

        String sql = "update " + SecurityConstants.T_ACCOUNT + " t set t.password = :password where t.login_name = :loginName";

        return this.genericJdbcDAO.update(sql, params) == 1;
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

        return this.genericJdbcDAO.genericInsert(role);
    }

    /**
     * 保存角色
     *
     * @param roleCode 角色code
     * @return
     */
    protected Role loadRole(String roleCode) {
        String sql = "select " + SecurityConstants.T_ROLE + " t where t.role_code = :roleCode";

        return this.genericJdbcDAO.queryForBean(sql, Collections.singletonMap("roleCode", roleCode), Role.class);
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
        sql = "delete from " + SecurityConstants.T_ACCOUNT_ROLE + " t where t.account = :account";
        this.genericJdbcDAO.update(sql, Collections.singletonMap("account", account));
        //2.保存现有的
        for (String code : roleCodes) {
            AccountRole accountRole = new AccountRole(account, code);
            try {
                this.genericJdbcDAO.genericInsert(accountRole);
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
        String sql = "select t.role_code from " + SecurityConstants.T_ACCOUNT_ROLE + " t where t.account = :loginName";

        return this.genericJdbcDAO.queryForList(sql, Collections.singletonMap("loginName", loginName), String.class);
    }
}

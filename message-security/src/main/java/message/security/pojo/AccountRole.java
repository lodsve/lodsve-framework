package message.security.pojo;

import message.jdbc.annontations.Column;
import message.jdbc.annontations.Id;
import message.security.SecurityConstants;

import javax.persistence.GeneratedValue;
import javax.persistence.Table;

/**
 * 账户角色关系.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 2014-12-7 14:25
 */
@Table(name = SecurityConstants.T_ACCOUNT_ROLE)
public class AccountRole {
    @Id
    @GeneratedValue(generator = SecurityConstants.S_SECURITY)
    private Long pkId;
    @Column
    private String account;
    @Column
    private String roleCode;

    public AccountRole() {
    }

    public AccountRole(String account, String roleCode) {
        this.account = account;
        this.roleCode = roleCode;
    }

    public Long getPkId() {
        return pkId;
    }

    public void setPkId(Long pkId) {
        this.pkId = pkId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }
}

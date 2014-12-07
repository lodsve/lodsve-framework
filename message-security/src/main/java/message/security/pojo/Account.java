package message.security.pojo;

import message.jdbc.annontations.Column;
import message.jdbc.annontations.Id;
import message.security.SecurityConstants;
import message.validate.annotations.Limit;
import message.validate.annotations.Password;
import message.validate.annotations.Regex;
import message.validate.annotations.ValidateEntity;

import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import java.util.Date;

/**
 * 账号.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 2014-12-5 20:07
 */
@Table(name = SecurityConstants.T_ACCOUNT)
@ValidateEntity
public class Account {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(generator = SecurityConstants.S_SECURITY)
    private Long pkId;
    /**
     * 登录名
     */
    @Column
    @Limit(min = 6, max = 16)
    @Regex(regex = SecurityConstants.REGEX_LOGINNAME)
    private String loginName;
    /**
     * 密码
     */
    @Column
    @Password(min = 6, max = 20, regex = SecurityConstants.REGEX_PASSWORD)
    private String password;
    /**
     * 注册时间
     */
    @Column
    private Date createDate;

    public Account() {
    }

    public Account(String password, String loginName) {
        this.password = password;
        this.loginName = loginName;
    }

    public Long getPkId() {
        return pkId;
    }

    public void setPkId(Long pkId) {
        this.pkId = pkId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}

package message.security.pojo;

import message.jdbc.annontations.Column;
import message.jdbc.annontations.Id;
import message.security.SecurityConstants;
import message.validate.annotations.English;
import message.validate.annotations.Limit;
import message.validate.annotations.ValidateEntity;

import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import java.util.Date;

/**
 * 角色.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 2014-12-7 14:10
 */
@Table(name = SecurityConstants.T_ROLE)
@ValidateEntity
public class Role {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(generator = SecurityConstants.S_SECURITY)
    private Long pkId;
    /**
     * 角色code
     */
    @Column
    @English
    @Limit(min = 3, max = 10)
    private String roleCode;
    /**
     * 创建时间
     */
    @Column
    private Date createTime;

    public Role(String roleCode) {
        this.roleCode = roleCode;
    }

    public Role() {
    }

    public Long getPkId() {
        return pkId;
    }

    public void setPkId(Long pkId) {
        this.pkId = pkId;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}

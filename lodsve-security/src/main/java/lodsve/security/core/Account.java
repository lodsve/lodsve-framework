package lodsve.security.core;

import java.io.Serializable;

/**
 * 账号.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2016-2-19 12:14
 */
public class Account implements Serializable {
    /**
     * 主键
     */
    private Long id;
    /**
     * 登录名
     */
    private String loginName;
    /**
     * 业务系统中的用户或者其他额外数据
     */
    private Object source;

    public Account() {
    }

    public Account(Long id, String loginName) {
        this.id = id;
        this.loginName = loginName;
    }

    public Account(Long id, String loginName, Object source) {
        this.id = id;
        this.loginName = loginName;
        this.source = source;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public Object getSource() {
        return source;
    }

    public void setSource(Object source) {
        this.source = source;
    }
}

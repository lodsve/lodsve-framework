package lodsve.base.email;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import lodsve.base.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 发送邮件时的身份认证器.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 13-3-25 上午6:53
 */
public class Authentication extends Authenticator {
    private static final Logger logger = LoggerFactory.getLogger(Authentication.class);

    /**用户名(发送者邮箱地址)**/
    private String username;
    /**发送者邮箱密码**/
    private String password;

    public Authentication(String username, String password) {
        if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password)){
            logger.error("username and password is required!");
            throw new RuntimeException("username and password is required!");
        }

        this.username = username;
        this.password = password;
    }

    protected PasswordAuthentication getPasswordAuthentication(){
        return new PasswordAuthentication(username, password);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

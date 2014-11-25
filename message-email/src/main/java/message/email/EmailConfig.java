package message.email;

/**
 * 邮箱服务器的配置.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 13-3-25 上午6:42
 */
public class EmailConfig {
    private static final long serialVersionUID = 1245472715337874695L;

    private String name;				//邮件名称
    private String smtp;				//smtp服务器名称
    private int port;					//端口号
    private String description; 		//描述

    public EmailConfig() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSmtp() {
        return smtp;
    }

    public void setSmtp(String smtp) {
        this.smtp = smtp;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

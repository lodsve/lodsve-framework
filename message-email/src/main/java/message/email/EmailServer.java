package message.email;

import message.base.utils.ApplicationHelper;
import message.utils.StringUtils;
import message.utils.ValidateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * 发送邮件服务器.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 13-3-25 上午6:19
 */
public class EmailServer {
    private static final Logger logger = LoggerFactory.getLogger(EmailServer.class);
    //spring中配置
    /**
     * 邮箱服务器配置*
     */
    private static List<EmailConfig> configs = new ArrayList<EmailConfig>();
    /**
     * 是否开启debug调试模式*
     */
    private boolean isDebug = false;
    /**
     * 是否启用身份验证*
     */
    private boolean isAuth = true;
    /**
     * 验证类型*
     */
    private String authType = "auth";
    /**
     * 是否初始化
     */
    private static boolean isInit = false;
    private static EmailServer emailServer;

    /**
     * 私有化默认构造器,使外部不可实例化
     */
    private EmailServer(boolean isAuth, boolean isDebug) {
        this.isAuth = isAuth;
        this.isDebug = isDebug;
    }

    /**
     * 单例,保证上下文中只有一个EmailServer
     *
     * @return EmailServer
     */
    public static EmailServer getInstance() {
        if (!isInit) {
            Map<String, EmailConfig> configsInContext = ApplicationHelper.getInstance().getBeansByType(EmailConfig.class);
            configs.addAll(configsInContext.values());

            isInit = true;
        }

        if (emailServer == null)
            emailServer = new EmailServer(true, false);

        return emailServer;
    }

    /**
     * 发送普通邮件(单个接收人)
     *
     * @param username 发件人用户名
     * @param password 发件人密码
     * @param title    邮件标题
     * @param content  邮件正文
     * @param receiver 单个接收人
     * @return
     */
    public boolean sendTextMail(String username, String password, String title, String content, String receiver) {
        return this.sendTextMail(username, password, title, content, Collections.singletonList(receiver));
    }

    /**
     * 发送普通邮件(多个接收人)
     *
     * @param username  发件人用户名
     * @param password  发件人密码
     * @param title     邮件标题
     * @param content   邮件正文
     * @param receivers 多个接收人
     * @return
     */
    public boolean sendTextMail(String username, String password, String title, String content, List<String> receivers) {
        return this.sendMail(username, password, title, content, receivers, false, null);
    }

    /**
     * 发送HTML邮件(单个接收人)
     *
     * @param username 发件人用户名
     * @param password 发件人密码
     * @param title    邮件标题
     * @param content  邮件正文
     * @param receiver 单个接收人
     * @return
     */
    public boolean sendHtmlMail(String username, String password, String title, String content, String receiver) {
        return this.sendHtmlMail(username, password, title, content, Collections.singletonList(receiver));
    }

    /**
     * 发送HTML邮件(多个接收人)
     *
     * @param username  发件人用户名
     * @param password  发件人密码
     * @param title     邮件标题
     * @param content   邮件正文
     * @param receivers 多个接收人
     * @return
     */
    public boolean sendHtmlMail(String username, String password, String title, String content, List<String> receivers) {
        return this.sendMail(username, password, title, content, receivers, true, null);
    }

    /**
     * 发送有附件的邮件
     *
     * @param username  发件人用户名
     * @param password  发件人密码
     * @param title     邮件标题
     * @param content   邮件正文
     * @param receivers 多个接收人
     * @param files     要发送的附件
     * @return
     */
    public boolean sendAttachmentMail(String username, String password, String title, String content, List<String> receivers, List<File> files) {
        return this.sendMail(username, password, title, content, receivers, true, files);
    }

    /**
     * 发送有附件的邮件
     *
     * @param username 发件人用户名
     * @param password 发件人密码
     * @param title    邮件标题
     * @param content  邮件正文
     * @param receiver 接收人
     * @param files    要发送的附件
     * @return
     */
    public boolean sendAttachmentMail(String username, String password, String title, String content, String receiver, List<File> files) {
        return this.sendAttachmentMail(username, password, title, content, Collections.singletonList(receiver), files);
    }

    /**
     * 发送有附件的邮件
     *
     * @param username 发件人用户名
     * @param password 发件人密码
     * @param title    邮件标题
     * @param content  邮件正文
     * @param receiver 接收人
     * @param file     要发送的附件
     * @return
     */
    public boolean sendAttachmentMail(String username, String password, String title, String content, String receiver, File file) {
        return this.sendAttachmentMail(username, password, title, content, Collections.singletonList(receiver), Collections.singletonList(file));
    }

    /**
     * 发送有附件的邮件
     *
     * @param username  发件人用户名
     * @param password  发件人密码
     * @param title     邮件标题
     * @param content   邮件正文
     * @param receivers 多个接收人
     * @param fileNames 要发送的附件
     * @return
     */
    public boolean sendAttachmentMail(String username, String password, String title, List<String> fileNames, String content, List<String> receivers) {
        List<File> fs = new ArrayList<File>();
        for (String fn : fileNames) {
            File f = new File(fn);
            if (f.exists() && f.canRead())
                fs.add(f);
        }

        return this.sendAttachmentMail(username, password, title, content, receivers, fs);
    }

    /**
     * 发送有附件的邮件
     *
     * @param username  发件人用户名
     * @param password  发件人密码
     * @param title     邮件标题
     * @param content   邮件正文
     * @param receivers 多个接收人
     * @param fileName  要发送的附件
     * @return
     */
    public boolean sendAttachmentMail(String username, String password, String title, String content, List<String> receivers, String fileName) {
        return this.sendAttachmentMail(username, password, title, Collections.singletonList(fileName), content, receivers);
    }

    /**
     * 发送有附件的邮件
     *
     * @param username 发件人用户名
     * @param password 发件人密码
     * @param title    邮件标题
     * @param content  邮件正文
     * @param receiver 多个接收人
     * @param fileName 要发送的附件
     * @return
     */
    public boolean sendAttachmentMail(String username, String password, String title, String content, String receiver, String fileName) {
        return this.sendAttachmentMail(username, password, title, Collections.singletonList(fileName), content, Collections.singletonList(receiver));
    }

    /**
     * 发送邮件(多个接收人)(可以指定是否是html邮件)
     *
     * @param username   发件人用户名
     * @param password   发件人密码
     * @param title      邮件标题
     * @param content    邮件正文
     * @param receivers  多个接收人
     * @param isHtmlMail 是否是html邮件
     * @param files      要发送的附件
     * @return
     */
    private boolean sendMail(String username, String password, String title, String content, List<String> receivers, boolean isHtmlMail, List<File> files) {
        Authentication auth = null;
        if (this.isAuth) {
            //如果需要身份认证，则创建一个密码验证器
            auth = new Authentication(username, password);
        }

        Properties props = new Properties();
        props.setProperty("mail.smtp.auth", this.isAuth ? "true" : "false");
        props.setProperty("mail.transport.protocol", "auth");
        EmailConfig config = this.getEmailConfig(username);
        props.setProperty("mail.smtp.host", config.getSmtp());
        props.setProperty("mail.smtp.port", config.getPort() + "");

        // 根据邮件会话属性和密码验证器构造一个发送邮件的session
        Session session = Session.getDefaultInstance(props, auth);
        session.setDebug(this.isDebug);

        Message message = null;
        if (files == null || files.isEmpty()) {
            //非发送附件
            message = this.makeSimpleMail(session, title, content, username, receivers, isHtmlMail);
        } else {
            //发送附件
            message = this.makeAttachmentMail(session, title, content, username, receivers, files);
        }

        try {
            Transport.send(message);

            return true;
        } catch (MessagingException e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 获取邮件服务器配置
     *
     * @param email 邮箱地址
     * @return
     */
    private EmailConfig getEmailConfig(String email) {
        String mailServiceDomainName = this.getMailServiceDomainName(email);
        for (EmailConfig config : configs) {
            if (config.getName().equals(mailServiceDomainName)) {
                return config;
            }
        }
        return null;
    }

    /**
     * 创建邮件message(无附件)
     *
     * @param session    根据配置获得的session
     * @param title      邮件主题
     * @param content    邮件的内容
     * @param from       发件者
     * @param receivers  收件者
     * @param isHtmlMail 是否是html邮件
     */
    private Message makeSimpleMail(Session session, String title, String content, String from, List<String> receivers, boolean isHtmlMail) {
        Message message = new MimeMessage(session);
        try {
            /**标题**/
            logger.info("this mail's title is {}", title);
            message.setSubject(title);
            /**内容**/
            logger.info("this mail's content is {}", content);
            if (isHtmlMail) {
                //是html邮件
                message.setContent(content, "text/html;charset=utf-8");
            } else {
                //普通邮件
                message.setText(content);
            }
            /**发件者地址**/
            logger.info("this mail's sender is {}", from);
            Address fromAddress = new InternetAddress(from);
            message.setFrom(fromAddress);
            /**接收者地址**/
            Address[] tos = new InternetAddress[receivers.size()];
            for (int i = 0; i < receivers.size(); i++) {
                String receiver = receivers.get(i);
                if (ValidateUtils.isEmail(receiver)) {
                    tos[i] = new InternetAddress(receiver);
                }
            }
            /**发件时间**/
            message.setSentDate(new Date());

            message.setRecipients(Message.RecipientType.TO, tos);
        } catch (MessagingException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }

        return message;
    }

    /**
     * 创建邮件message(有附件)
     *
     * @param session   根据配置获得的session
     * @param title     邮件主题
     * @param content   邮件的内容
     * @param from      发件者
     * @param receivers 收件者
     * @param files     附件
     */
    private Message makeAttachmentMail(Session session, String title, String content, String from, List<String> receivers, List<File> files) {
        Message message = new MimeMessage(session);
        try {
            /**标题**/
            logger.info("this mail's title is {}", title);
            message.setSubject(title);
            /**内容**/
            logger.info("this mail's content is {}", content);
            Multipart multipart = new MimeMultipart();
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(content, "text/html;charset=utf-8");
            multipart.addBodyPart(mimeBodyPart);
            if (files != null && !files.isEmpty()) {
                //存在附件
                for (Iterator<File> it = files.iterator(); it.hasNext(); ) {
                    mimeBodyPart = new MimeBodyPart();
                    File attachment = it.next();
                    //得到数据源
                    FileDataSource fds = new FileDataSource(attachment);
                    //得到附件本身并至入BodyPart
                    mimeBodyPart.setDataHandler(new DataHandler(fds));
                    //得到文件名同样至入BodyPart(附件名乱码解决方案MimeUtility.encodeText(name))
                    mimeBodyPart.setFileName(MimeUtility.encodeText(attachment.getName()));
                    multipart.addBodyPart(mimeBodyPart);
                }
            }

            message.setContent(multipart);
            /**发件者地址**/
            logger.info("this mail's sender is {}", from);
            Address fromAddress = new InternetAddress(from);
            message.setFrom(fromAddress);
            /**接收者地址**/
            Address[] tos = new InternetAddress[receivers.size()];
            for (int i = 0; i < receivers.size(); i++) {
                String receiver = receivers.get(i);
                if (ValidateUtils.isEmail(receiver)) {
                    tos[i] = new InternetAddress(receiver);
                }
            }
            /**发件时间**/
            message.setSentDate(new Date());

            message.setRecipients(Message.RecipientType.TO, tos);
        } catch (MessagingException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return message;
    }

    /**
     * 获取邮箱域名
     *
     * @param email 邮箱
     * @return
     */
    private String getMailServiceDomainName(String email) {
        if (StringUtils.isEmpty(email)) {
            return "";
        } else {
            int firstIndex = StringUtils.indexOf(email, "@");
            int secondIndex = StringUtils.lastIndexOf(email, ".");

            return StringUtils.substring(email, firstIndex + 1, secondIndex);
        }
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }
}

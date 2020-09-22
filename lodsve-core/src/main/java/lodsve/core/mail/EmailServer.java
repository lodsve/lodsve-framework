/*
 * Copyright © 2009 Sun.Hao(https://www.crazy-coder.cn/)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lodsve.core.mail;

import com.google.common.collect.Lists;
import lodsve.core.configuration.MailProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.CollectionUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.List;

/**
 * 发送邮件服务器.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 13-3-25 上午6:19
 */
public class EmailServer {
    private static final Logger logger = LoggerFactory.getLogger(EmailServer.class);

    private final JavaMailSender mailSender;
    private final MailProperties properties;

    public EmailServer(JavaMailSender mailSender, MailProperties properties) {
        this.mailSender = mailSender;
        this.properties = properties;
    }

    /**
     * 发送普通邮件
     *
     * @param title    邮件标题
     * @param content  邮件正文（可以为html邮件内容）
     * @param receiver 接收人
     */
    public void sendTextMail(String title, String content, String... receiver) {
        sendMail(title, content, Lists.newArrayList(receiver), Lists.newArrayList());
    }

    /**
     * 发送有附件的邮件
     *
     * @param title    邮件标题
     * @param content  邮件正文（可以为html邮件内容）
     * @param files    要发送的附件
     * @param receiver 接收人
     */
    public void sendAttachmentMail(String title, String content, List<File> files, String... receiver) {
        sendMail(title, content, Lists.newArrayList(receiver), files);
    }

    /**
     * 发送邮件
     *
     * @param title     邮件标题
     * @param content   邮件正文
     * @param receivers 多个接收人
     * @param files     要发送的附件
     */
    private void sendMail(String title, String content, List<String> receivers, List<File> files) {
        MimeMessage mailMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true);
            helper.setFrom(properties.getUsername());
            helper.setTo(receivers.toArray(new String[0]));
            helper.setSubject(title);
            helper.setText(content, true);

            // 如果存在附件
            if (!CollectionUtils.isEmpty(files)) {
                files.forEach(f -> {
                    try {
                        helper.addAttachment(f.getName(), f);
                    } catch (MessagingException e) {
                        if (logger.isErrorEnabled()) {
                            logger.error(e.getMessage());
                        }
                    }
                });
            }
        } catch (MessagingException e) {
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage());
            }
        }

        mailSender.send(mailMessage);
    }
}

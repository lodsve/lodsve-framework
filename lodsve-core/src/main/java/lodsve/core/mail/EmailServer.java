/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package lodsve.core.mail;

import com.google.common.collect.Lists;
import lodsve.core.configuration.properties.MailProperties;
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

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

import lodsve.core.condition.ConditionalOnProperty;
import lodsve.core.condition.ConditionalOnSingleCandidate;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;

/**
 * .
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 */
@Configuration
@ConditionalOnProperty(key = "lodsve.mail.test-connection", value = "true")
@ConditionalOnSingleCandidate(JavaMailSenderImpl.class)
public class MailSenderValidatorAutoConfiguration {
    private final JavaMailSenderImpl mailSender;

    public MailSenderValidatorAutoConfiguration(ObjectProvider<JavaMailSenderImpl> mailSender) {
        this.mailSender = mailSender.getIfAvailable();
    }

    @PostConstruct
    public void validateConnection() {
        try {
            this.mailSender.testConnection();
        } catch (MessagingException ex) {
            throw new IllegalStateException("Mail server is not available", ex);
        }
    }
}

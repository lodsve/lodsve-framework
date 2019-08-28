/*
 * Copyright (C) 2019 Sun.Hao(https://www.crazy-coder.cn/)
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package lodsve.core.mail;

import lodsve.core.condition.ConditionalOnClass;
import lodsve.core.condition.ConditionalOnJndi;
import lodsve.core.condition.ConditionalOnMissingBean;
import lodsve.core.condition.ConditionalOnProperty;
import lodsve.core.configuration.MailProperties;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jndi.JndiLocatorDelegate;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.mail.Session;
import javax.naming.NamingException;

/**
 * Configure a {@link MailSender} based on a {@link Session} available on JNDI.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 */
@Configuration
@ConditionalOnClass(Session.class)
@ConditionalOnProperty(key = "lodsve.mail.jndi-name", notNull = true)
@ConditionalOnJndi
public class MailSenderJndiConfiguration {
    private final MailProperties properties;

    MailSenderJndiConfiguration(ObjectProvider<MailProperties> properties) {
        this.properties = properties.getIfAvailable();
    }

    @Bean
    public JavaMailSenderImpl mailSender(Session session) {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setDefaultEncoding(this.properties.getDefaultEncoding());
        sender.setSession(session);
        return sender;
    }

    @Bean
    @ConditionalOnMissingBean
    public Session session() {
        String jndiName = this.properties.getJndiName();
        try {
            return JndiLocatorDelegate.createDefaultResourceRefLocator().lookup(jndiName, Session.class);
        } catch (NamingException ex) {
            throw new IllegalStateException(String.format("Unable to find Session in JNDI location %s", jndiName), ex);
        }
    }
}

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

import lodsve.core.condition.ConditionalOnClass;
import lodsve.core.condition.ConditionalOnJndi;
import lodsve.core.condition.ConditionalOnMissingBean;
import lodsve.core.condition.ConditionalOnProperty;
import lodsve.core.configuration.properties.MailProperties;
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

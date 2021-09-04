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

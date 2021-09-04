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

import lodsve.core.autoproperties.relaxedbind.annotations.EnableConfigurationProperties;
import lodsve.core.condition.ConditionalOnClass;
import lodsve.core.condition.ConditionalOnMissingBean;
import lodsve.core.configuration.properties.MailProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;

import javax.activation.MimeType;
import javax.mail.internet.MimeMessage;

/**
 * Configuration for email support.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 */
@Configuration
@ConditionalOnClass({MimeMessage.class, MimeType.class, MailSender.class})
@ConditionalOnMissingBean(MailSender.class)
@EnableConfigurationProperties(MailProperties.class)
@Import({MailSenderJndiConfiguration.class, MailSenderPropertiesConfiguration.class, MailSenderValidatorAutoConfiguration.class})
public class MailSenderConfiguration {
    @Bean
    public EmailServer emailServer(JavaMailSender sender, MailProperties properties) {
        return new EmailServer(sender, properties);
    }
}

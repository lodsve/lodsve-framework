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
package lodsve.filesystem.config;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import lodsve.core.autoproperties.relaxedbind.annotations.EnableConfigurationProperties;
import lodsve.core.condition.ConditionalOnProperty;
import lodsve.filesystem.handler.AliyunOssFileSystemHandler;
import lodsve.filesystem.handler.AmazonS3FileSystemHandler;
import lodsve.filesystem.handler.FileSystemHandler;
import lodsve.filesystem.properties.FileSystemProperties;
import lodsve.filesystem.server.FileSystemServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 文件上传组件配置.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 */
@Configuration
@EnableConfigurationProperties(FileSystemProperties.class)
public class FileSystemAutoConfiguration {
    @Bean
    public FileSystemServer fileSystemTemplate(FileSystemHandler handler) {
        return new FileSystemServer(handler);
    }

    @Configuration
    @ConditionalOnProperty(key = "lodsve.file-system.type", value = "OSS")
    public static class OssConfiguration {
        @Bean
        public ClientBuilderConfiguration clientConfiguration(FileSystemProperties properties) {
            FileSystemProperties.ClientExtendProperties client = properties.getClient();
            ClientBuilderConfiguration configuration = new ClientBuilderConfiguration();
            configuration.setMaxConnections(client.getMaxConnections());
            configuration.setSocketTimeout(client.getSocketTimeout());
            configuration.setConnectionTimeout(client.getConnectionTimeout());
            configuration.setConnectionRequestTimeout(client.getConnectionRequestTimeout());
            client.setIdleConnectionTime(client.getIdleConnectionTime());
            configuration.setMaxErrorRetry(client.getMaxErrorRetry());
            configuration.setSupportCname(client.isSupportCname());
            configuration.setSLDEnabled(client.isSldEnabled());
            if (com.aliyun.oss.common.comm.Protocol.HTTP.toString().equals(client.getProtocol())) {
                configuration.setProtocol(com.aliyun.oss.common.comm.Protocol.HTTP);
            } else if (com.aliyun.oss.common.comm.Protocol.HTTPS.toString().equals(client.getProtocol())) {
                configuration.setProtocol(com.aliyun.oss.common.comm.Protocol.HTTPS);
            }
            configuration.setUserAgent(client.getUserAgent());
            return configuration;
        }

        @Bean
        public FileSystemHandler fileSystemHandler(FileSystemProperties properties, ClientBuilderConfiguration configuration) {
            return new AliyunOssFileSystemHandler(properties, configuration);
        }
    }

    @Configuration
    @ConditionalOnProperty(key = "lodsve.file-system.type", value = "AWS")
    public static class AwsConfiguration {
        @Bean
        public com.amazonaws.ClientConfiguration awsConfiguration(FileSystemProperties properties) {
            FileSystemProperties.ClientExtendProperties client = properties.getClient();

            ClientConfiguration configuration = new ClientConfiguration();
            configuration.setMaxConnections(client.getMaxConnections());
            configuration.setSocketTimeout(client.getSocketTimeout());
            configuration.setConnectionTimeout(client.getConnectionTimeout());
            client.setIdleConnectionTime(client.getIdleConnectionTime());
            configuration.setMaxErrorRetry(client.getMaxErrorRetry());
            if (Protocol.HTTP.toString().equals(client.getProtocol())) {
                configuration.setProtocol(Protocol.HTTP);
            } else if (Protocol.HTTPS.toString().equals(client.getProtocol())) {
                configuration.setProtocol(Protocol.HTTPS);
            }
            return configuration;
        }

        @Bean
        public FileSystemHandler fileServerClient(FileSystemProperties properties, ClientConfiguration configuration) {
            return new AmazonS3FileSystemHandler(properties, configuration);
        }
    }
}

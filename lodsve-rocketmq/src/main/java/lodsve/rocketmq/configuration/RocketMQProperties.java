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
package lodsve.rocketmq.configuration;

import lodsve.core.autoproperties.relaxedbind.annotations.ConfigurationProperties;
import lombok.Getter;
import lombok.Setter;

/**
 * rocketMQ consumer配置.
 *
 * @author 孙昊(Hulk)
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "lodsve.rocketmq", locations = "${params.root}/framework/rocketmq.properties")
public class RocketMQProperties {
    private String nameServer;
    private String charset = "UTF-8";
    private Producer producer;
    private Consumer consumer;

    @Setter
    @Getter
    public static class Consumer {
        private String group = "DefaultConsumer";
    }

    @Setter
    @Getter
    public static class Producer {
        private String group;
        /**
         * Millis of send message timeout.
         */
        private int sendMessageTimeout = 3000;
        /**
         * Compress message body threshold, namely, message body larger than 4k will be compressed on default.
         */
        private int compressMessageBodyThreshold = 1024 * 4;
        /**
         * Maximum number of retry to perform internally before claiming sending failure in synchronous mode.
         * This may potentially cause message duplication which is up to application developers to resolve.
         */
        private int retryTimesWhenSendFailed = 2;
        /**
         * <p> Maximum number of retry to perform internally before claiming sending failure in asynchronous mode. </p>
         * This may potentially cause message duplication which is up to application developers to resolve.
         */
        private int retryTimesWhenSendAsyncFailed = 2;
        /**
         * Indicate whether to retry another broker on sending failure internally.
         */
        private boolean retryNextServer = false;
        /**
         * Maximum allowed message size in bytes.
         */
        private int maxMessageSize = 1024 * 1024 * 4;
    }
}

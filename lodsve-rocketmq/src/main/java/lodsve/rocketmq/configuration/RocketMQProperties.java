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
package lodsve.rocketmq.configuration;

import lodsve.core.properties.relaxedbind.annotations.ConfigurationProperties;
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

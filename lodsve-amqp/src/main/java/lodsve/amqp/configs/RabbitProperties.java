/*
 * Copyright (C) 2018  Sun.Hao
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

package lodsve.amqp.configs;

import lodsve.core.properties.relaxedbind.annotations.ConfigurationProperties;
import lodsve.core.properties.relaxedbind.annotations.Required;

import java.util.Map;

/**
 * rabbit mq base properties.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2016-01-15 12:00
 */
@ConfigurationProperties(prefix = "lodsve.rabbit", locations = "${params.root}/framework/rabbit.properties")
public class RabbitProperties {
    @Required
    private String address;
    @Required
    private String username;
    @Required
    private String password;
    /**
     * false: 异常消息直接丢弃
     * true: 异常消息将被重新传递
     */
    private Boolean requeueRejected = true;

    private Map<String, QueueConfig> queues;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getRequeueRejected() {
        return requeueRejected;
    }

    public void setRequeueRejected(Boolean requeueRejected) {
        this.requeueRejected = requeueRejected;
    }

    public Map<String, QueueConfig> getQueues() {
        return queues;
    }

    public void setQueues(Map<String, QueueConfig> queues) {
        this.queues = queues;
    }
}

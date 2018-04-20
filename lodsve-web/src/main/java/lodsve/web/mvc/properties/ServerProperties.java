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

package lodsve.web.mvc.properties;

import lodsve.core.properties.relaxedbind.annotations.ConfigurationProperties;
import lodsve.core.properties.relaxedbind.annotations.Required;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统配置.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @date 15/10/10 下午10:01
 */
@ConfigurationProperties(prefix = "lodsve.server", locations = "${params.root}/framework/server.properties")
public class ServerProperties {
    /**
     * 前台URL
     */
    @Required
    private String frontEndUrl;
    /**
     * 服务端URL
     */
    @Required
    private String serverUrl;
    /**
     * debug配置
     */
    private Debug debug;
    /**
     * 是否启用验证码
     */
    private boolean enableCaptcha = false;
    /**
     * 验证码在session中的key
     */
    private String captchaKey = "captchaKey";
    /**
     * 请求验证码的路径
     */
    private String path = "/captcha";

    public String getFrontEndUrl() {
        return frontEndUrl;
    }

    public void setFrontEndUrl(String frontEndUrl) {
        this.frontEndUrl = frontEndUrl;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public Debug getDebug() {
        return debug;
    }

    public void setDebug(Debug debug) {
        this.debug = debug;
    }

    public boolean isEnableCaptcha() {
        return enableCaptcha;
    }

    public void setEnableCaptcha(boolean enableCaptcha) {
        this.enableCaptcha = enableCaptcha;
    }

    public String getCaptchaKey() {
        return captchaKey;
    }

    public void setCaptchaKey(String captchaKey) {
        this.captchaKey = captchaKey;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public static class Debug {
        /**
         * 需要忽略的url
         */
        private List<String> excludeUrl = new ArrayList<>(0);
        /**
         * 需要忽略的ip/address
         */
        private List<String> excludeAddress = new ArrayList<>(0);
        /**
         * 当执行时间超长，将会警告
         */
        private long maxProcessingTime = 3000;

        public List<String> getExcludeUrl() {
            return excludeUrl;
        }

        public void setExcludeUrl(List<String> excludeUrl) {
            this.excludeUrl = excludeUrl;
        }

        public List<String> getExcludeAddress() {
            return excludeAddress;
        }

        public void setExcludeAddress(List<String> excludeAddress) {
            this.excludeAddress = excludeAddress;
        }

        public long getMaxProcessingTime() {
            return maxProcessingTime;
        }

        public void setMaxProcessingTime(long maxProcessingTime) {
            this.maxProcessingTime = maxProcessingTime;
        }
    }
}

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
package lodsve.web.mvc.properties;

import lodsve.core.autoproperties.relaxedbind.annotations.ConfigurationProperties;
import lodsve.core.autoproperties.relaxedbind.annotations.Required;
import lombok.Getter;
import lombok.Setter;

/**
 * 系统配置.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 15/10/10 下午10:01
 */
@Setter
@Getter
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
    private DebugConfig debug;
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
}

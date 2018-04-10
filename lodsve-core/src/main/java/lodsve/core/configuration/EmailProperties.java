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

package lodsve.core.configuration;

import lodsve.core.email.EmailBean;
import lodsve.core.properties.relaxedbind.annotations.ConfigurationProperties;

import java.util.Map;

/**
 * 邮箱服务器的配置.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/5/26 下午2:57
 */
@ConfigurationProperties(prefix = "lodsve.email", locations = "classpath:/META-INF/email.properties")
public class EmailProperties {
    private Map<String, EmailBean> beans;

    public Map<String, EmailBean> getBeans() {
        return beans;
    }

    public void setBeans(Map<String, EmailBean> beans) {
        this.beans = beans;
    }
}

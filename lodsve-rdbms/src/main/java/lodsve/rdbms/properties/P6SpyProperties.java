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

package lodsve.rdbms.properties;

import lodsve.core.io.support.LodsveResourceLoader;
import lodsve.core.properties.relaxedbind.annotations.ConfigurationProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.io.Resource;

/**
 * Spy Properties.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2017/12/25 下午11:53
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "lodsve.p6spy", locations = "${params.root}/framework/p6spy.properties")
public class P6SpyProperties {
    /**
     * spy配置
     */
    private Resource config = new LodsveResourceLoader().getResource("classpath:/META-INF/p6spy/spy.properties");
}

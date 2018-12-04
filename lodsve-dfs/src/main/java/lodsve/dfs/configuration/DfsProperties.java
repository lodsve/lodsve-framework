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

package lodsve.dfs.configuration;

import lodsve.core.properties.relaxedbind.annotations.ConfigurationProperties;
import lombok.Getter;
import lombok.Setter;
import org.csource.common.FastDfsConfig;

/**
 * 配置.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2017-12-4-0004 10:46
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "lodsve.dfs", locations = "${params.root}/framework/dfs.properties")
public class DfsProperties {
    private FastDfsConfig fastDfs;
    private TfsConfig tfs;
    private GoogleFsConfig google;
    private NormalFsConfig normal;

    @Setter
    @Getter
    public static class TfsConfig {
    }

    @Setter
    @Getter
    public static class GoogleFsConfig {
    }

    @Setter
    @Getter
    public static class NormalFsConfig {
        private String store;
    }
}

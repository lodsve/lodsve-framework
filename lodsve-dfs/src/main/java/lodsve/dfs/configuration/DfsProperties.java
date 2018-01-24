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

import org.csource.common.FastDfsConfig;
import lodsve.core.properties.relaxedbind.annotations.ConfigurationProperties;

/**
 * 配置.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version 1.0 2017-12-4-0004 10:46
 */
@ConfigurationProperties(prefix = "lodsve.dfs", locations = "${params.root}/framework/dfs.properties")
public class DfsProperties {
    private FastDfsConfig fastDfs;
    private TfsConfig tfs;
    private GoogleFsConfig google;
    private NormalFsConfig normal;

    public FastDfsConfig getFastDfs() {
        return fastDfs;
    }

    public void setFastDfs(FastDfsConfig fastDfs) {
        this.fastDfs = fastDfs;
    }

    public TfsConfig getTfs() {
        return tfs;
    }

    public void setTfs(TfsConfig tfs) {
        this.tfs = tfs;
    }

    public GoogleFsConfig getGoogle() {
        return google;
    }

    public void setGoogle(GoogleFsConfig google) {
        this.google = google;
    }

    public NormalFsConfig getNormal() {
        return normal;
    }

    public void setNormal(NormalFsConfig normal) {
        this.normal = normal;
    }

    public static class TfsConfig {
    }

    public static class GoogleFsConfig {
    }

    public static class NormalFsConfig {
        private String store;

        public String getStore() {
            return store;
        }

        public void setStore(String store) {
            this.store = store;
        }
    }
}

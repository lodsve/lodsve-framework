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

import lodsve.core.properties.relaxedbind.annotations.ConfigurationProperties;

/**
 * 系统配置.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2016/12/12 下午3:05
 */
@ConfigurationProperties(prefix = "application", locations = "${params.root}/framework/application.properties")
public class ApplicationProperties {
    /**
     * 开发模式
     */
    private boolean devMode = true;
    /**
     * 编码
     */
    private String encoding = "UTF-8";
    /**
     * whether the specified encoding is supposed to
     * override existing request and response encodings
     */
    private boolean forceEncoding = true;
    /**
     * banner配置
     */
    private BannerConfig banner;
    /**
     * 多线程配置
     */
    private ThreadConfig thread;

    public boolean isDevMode() {
        return devMode;
    }

    public void setDevMode(boolean devMode) {
        this.devMode = devMode;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public boolean isForceEncoding() {
        return forceEncoding;
    }

    public void setForceEncoding(boolean forceEncoding) {
        this.forceEncoding = forceEncoding;
    }

    public BannerConfig getBanner() {
        return banner;
    }

    public void setBanner(BannerConfig banner) {
        this.banner = banner;
    }

    public ThreadConfig getThread() {
        return thread;
    }

    public void setThread(ThreadConfig thread) {
        this.thread = thread;
    }
}

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
 * @author sunhao(sunhao.java @ gmail.com)
 * @version 1.0 2016/12/12 下午3:05
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

    public static class ThreadConfig {
        private int corePoolSize = 1;
        private int maxPoolSize = Integer.MAX_VALUE;
        private int keepAliveSeconds = 60;
        private boolean allowCoreThreadTimeOut = false;
        private int queueCapacity = Integer.MAX_VALUE;
        private boolean exposeUnconfigurableExecutor = false;

        public int getCorePoolSize() {
            return corePoolSize;
        }

        public void setCorePoolSize(int corePoolSize) {
            this.corePoolSize = corePoolSize;
        }

        public int getMaxPoolSize() {
            return maxPoolSize;
        }

        public void setMaxPoolSize(int maxPoolSize) {
            this.maxPoolSize = maxPoolSize;
        }

        public int getKeepAliveSeconds() {
            return keepAliveSeconds;
        }

        public void setKeepAliveSeconds(int keepAliveSeconds) {
            this.keepAliveSeconds = keepAliveSeconds;
        }

        public boolean isAllowCoreThreadTimeOut() {
            return allowCoreThreadTimeOut;
        }

        public void setAllowCoreThreadTimeOut(boolean allowCoreThreadTimeOut) {
            this.allowCoreThreadTimeOut = allowCoreThreadTimeOut;
        }

        public int getQueueCapacity() {
            return queueCapacity;
        }

        public void setQueueCapacity(int queueCapacity) {
            this.queueCapacity = queueCapacity;
        }

        public boolean isExposeUnconfigurableExecutor() {
            return exposeUnconfigurableExecutor;
        }

        public void setExposeUnconfigurableExecutor(boolean exposeUnconfigurableExecutor) {
            this.exposeUnconfigurableExecutor = exposeUnconfigurableExecutor;
        }
    }

    public static class BannerConfig {
        private boolean enable = true;
        private BannerMode mode = BannerMode.CONSOLE;
        private String charset = "UTF-8";
        private String location = "banner.txt";
        private Image image;

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }

        public BannerMode getMode() {
            return mode;
        }

        public void setMode(BannerMode mode) {
            this.mode = mode;
        }

        public String getCharset() {
            return charset;
        }

        public void setCharset(String charset) {
            this.charset = charset;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public Image getImage() {
            return image;
        }

        public void setImage(Image image) {
            this.image = image;
        }
    }

    public static class Image {
        private String location;
        private int width = 76;
        private int height = 0;
        private int margin = 2;
        private boolean invert = false;

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getMargin() {
            return margin;
        }

        public void setMargin(int margin) {
            this.margin = margin;
        }

        public boolean isInvert() {
            return invert;
        }

        public void setInvert(boolean invert) {
            this.invert = invert;
        }
    }

    public enum BannerMode {
        /**
         * 控制台打印
         */
        CONSOLE,
        /**
         * 日志
         */
        LOGGER
    }
}

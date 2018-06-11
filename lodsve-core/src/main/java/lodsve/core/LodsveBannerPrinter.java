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

package lodsve.core;

import lodsve.core.configuration.ApplicationProperties;
import lodsve.core.configuration.BannerConfig;
import lodsve.core.configuration.BannerMode;
import lodsve.core.io.support.LodsveResourceLoader;
import lodsve.core.properties.relaxedbind.RelaxedBindFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StringUtils;
import org.springframework.web.WebApplicationInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * 模仿spring-boot打印出banner.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2018/1/11 下午10:13
 */
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class LodsveBannerPrinter implements WebApplicationInitializer {
    private static final Logger logger = LoggerFactory.getLogger(LodsveBannerPrinter.class);

    private static final Banner DEFAULT_BANNER = new LodsveBanner();
    private static final String[] IMAGE_EXTENSION = {"gif", "jpg", "png"};
    private static final ResourceLoader RESOURCE_LOADER = new LodsveResourceLoader();

    private BannerConfig bannerConfig;

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        ApplicationProperties properties = new RelaxedBindFactory.Builder<>(ApplicationProperties.class).build();
        bannerConfig = properties.getBanner();

        if (!bannerConfig.isEnable()) {
            // no banners!
            return;
        }

        Banner banner = getBanner();

        if (BannerMode.LOGGER.equals(bannerConfig.getMode())) {
            printInLogger(banner);
            return;
        }

        printBannerInConsole(banner);
    }

    private void printInLogger(Banner banner) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        banner.print(bannerConfig, new PrintStream(output));
        try {
            String bannerContent = output.toString(bannerConfig.getCharset());

            logger.info(bannerContent);
        } catch (UnsupportedEncodingException e) {
            logger.warn("Failed to create String for banner", e);
        }
    }

    private void printBannerInConsole(Banner banner) {
        banner.print(bannerConfig, System.out);
    }

    private Banner getBanner() {
        Banners banners = new Banners();
        banners.addIfNotNull(getImageBanner(bannerConfig));
        banners.addIfNotNull(getTextBanner(bannerConfig));
        if (banners.hasAtLeastOneBanner()) {
            return banners;
        }
        return DEFAULT_BANNER;
    }

    private Banner getImageBanner(BannerConfig bannerConfig) {
        String location = bannerConfig.getImage().getLocation();
        if (StringUtils.hasLength(location)) {
            Resource resource = RESOURCE_LOADER.getResource(location);
            return (resource.exists() ? new ImageBanner(resource) : null);
        }

        for (String ext : IMAGE_EXTENSION) {
            Resource resource = RESOURCE_LOADER.getResource("banner." + ext);
            if (resource.exists()) {
                return new ImageBanner(resource);
            }
        }

        return null;
    }

    private Banner getTextBanner(BannerConfig bannerConfig) {
        String location = bannerConfig.getLocation();
        Resource resource = RESOURCE_LOADER.getResource(location);
        if (resource.exists()) {
            return new TextBanner(resource);
        }

        return null;
    }

    static class Banners implements Banner {
        private List<Banner> banners = new ArrayList<>(16);

        @Override
        public void print(BannerConfig config, PrintStream out) {
            for (Banner banner : banners) {
                banner.print(config, out);
            }
        }

        void addIfNotNull(Banner banner) {
            if (banner != null) {
                banners.add(banner);
            }
        }

        boolean hasAtLeastOneBanner() {
            return banners.size() > 0;
        }
    }
}

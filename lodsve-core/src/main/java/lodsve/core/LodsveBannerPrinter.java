package lodsve.core;

import lodsve.core.io.support.LodsveResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.WebApplicationInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 模仿spring-boot打印出banner.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version 1.0 2018/1/11 下午10:13
 */
public class LodsveBannerPrinter implements WebApplicationInitializer {
    private static final Banner DEFAULT_BANNER = new LodsveBanner();
    private static final String DEFAULT_BANNER_TEXT_NAME = "banner.txt";
    private static final String[] IMAGE_EXTENSION = {"gif", "jpg", "png"};
    private static final ResourceLoader RESOURCE_LOADER = new LodsveResourceLoader();

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        Banner banner = getBanner();
        banner.print(System.out);
    }

    private Banner getBanner() {
        Banners banners = new Banners();
        banners.addIfNotNull(getImageBanner());
        banners.addIfNotNull(getTextBanner());
        if (banners.hasAtLeastOneBanner()) {
            return banners;
        }
        return DEFAULT_BANNER;
    }

    private Banner getImageBanner() {
        for (String ext : IMAGE_EXTENSION) {
            Resource resource = RESOURCE_LOADER.getResource("banner." + ext);
            if (resource.exists()) {
                return new ImageBanner(resource);
            }
        }

        return null;
    }

    private Banner getTextBanner() {
        Resource resource = RESOURCE_LOADER.getResource(DEFAULT_BANNER_TEXT_NAME);
        if (resource.exists()) {
            return new TextBanner(resource);
        }

        return null;
    }

    static class Banners implements Banner {
        private List<Banner> banners = new ArrayList<>(16);

        @Override
        public void print(PrintStream out) {
            for (Banner banner : banners) {
                banner.print(out);
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

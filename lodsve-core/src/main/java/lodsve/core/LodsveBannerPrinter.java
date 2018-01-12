package lodsve.core;

import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 模仿spring-boot打印出banner.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version 1.0 2018/1/11 下午10:13
 */
@HandlesTypes(Banner.class)
public class LodsveBannerPrinter implements ServletContainerInitializer {

    @Override
    public void onStartup(Set<Class<?>> classSet, ServletContext servletContext) throws ServletException {
        if (classSet == null) {
            servletContext.log("No Banner in classpath to print!");
            return;
        }

        List<Banner> banners = new ArrayList<>(classSet.size());
        for (Class<?> clazz : classSet) {
            if (!clazz.isInterface() && !Modifier.isAbstract(clazz.getModifiers()) && Banner.class.isAssignableFrom(clazz)) {
                try {
                    banners.add((Banner) clazz.newInstance());
                } catch (Throwable ex) {
                    throw new ServletException("Failed to instantiate Banner class", ex);
                }
            }
        }

        if (banners.isEmpty()) {
            servletContext.log("No Banner in classpath to print!");
            return;
        }

        AnnotationAwareOrderComparator.sort(banners);
        servletContext.log("banners detected on classpath: " + banners);

        for (Banner banner : banners) {
            banner.print(System.out);
        }
    }
}

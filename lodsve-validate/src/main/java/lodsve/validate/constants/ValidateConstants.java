package lodsve.validate.constants;

import lodsve.core.properties.i18n.ResourceBundleHolder;
import lodsve.core.utils.PropertyPlaceholderHelper;
import lodsve.core.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * 验证框架的静态常量类.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0
 * @createTime 12-12-2 上午1:01
 */
public class ValidateConstants {
    private static final Logger logger = LoggerFactory.getLogger(ValidateConstants.class);
    private static ResourceBundleHolder resourceBundleHolder = new ResourceBundleHolder();

    static {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        List<Resource> resources = new ArrayList<>();
        try {
            resources.addAll(Arrays.asList(resolver.getResources("classpath:/META-INF/message/*.properties")));
        } catch (IOException e) {
            logger.error("resolver resource:'{classpath:/META-INF/message/*.properties}' is error!", e);
            e.printStackTrace();
        }

        for (Resource r : resources) {
            String filePath;
            try {
                filePath = r.getURL().toString();
            } catch (IOException e) {
                continue;
            }
            String fileName = r.getFilename();

            if (!StringUtils.contains(fileName, "_")) {
                resourceBundleHolder.loadMessageResource(filePath, 1);
            }
        }
    }

    public static String getMessage(String key, Object... args) {
        String message = resourceBundleHolder.getResourceBundle(getLocale()).getString(key);

        return PropertyPlaceholderHelper.replaceNumholder(message, message, args);
    }

    private static Locale getLocale() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null || !(requestAttributes instanceof ServletRequestAttributes)) {
            return Locale.getDefault();
        }

        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        return request != null ? request.getLocale() : Locale.getDefault();
    }

    /**
     * 框架中定义的注解
     */
    public static final String[] VALIDATE_ANNOTATIONS = new String[]{
            "Chinese", "Double", "Email", "English", "IdCard", "Integer", "Ip", "Limit", "Mobile",
            "NotNull", "Number", "Password", "Qq", "Regex", "Telephone", "Url", "Zip"
    };
}

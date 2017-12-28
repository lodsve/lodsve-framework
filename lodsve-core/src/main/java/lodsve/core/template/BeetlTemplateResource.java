package lodsve.core.template;

import lodsve.core.utils.StringUtils;
import org.apache.commons.io.IOUtils;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.StringTemplateResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.util.Map;

/**
 * Beetl Template Resource.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 2017-12-28-0028 12:31
 */
public class BeetlTemplateResource extends AbstractTemplateResource {
    private static final Logger logger = LoggerFactory.getLogger(BeetlTemplateResource.class);

    public BeetlTemplateResource(String template, Map<String, Object> context) {
        super(template, context, "resource load by Beetl!");
    }

    @Override
    public String renderTemplate() {
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource(template);
        if (!resource.exists()) {
            throw new IllegalArgumentException(String.format("'%s' is not found!", template));
        }

        String content;

        try {
            content = IOUtils.toString(resource.getInputStream());
        } catch (IOException e) {
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage(), e);
            }
            throw new IllegalArgumentException(String.format("toString for '%s' has any exception!!", template));
        }

        try {
            StringTemplateResourceLoader templateResourceLoader = new StringTemplateResourceLoader();
            GroupTemplate template = new GroupTemplate(templateResourceLoader, Configuration.defaultConfiguration());
            Template t = template.getTemplate(content);
            t.binding(context);

            return t.render();
        } catch (IOException e) {
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage(), e);
            }

            return StringUtils.EMPTY;
        }
    }
}

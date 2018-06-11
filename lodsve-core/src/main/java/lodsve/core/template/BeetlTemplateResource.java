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
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2017-12-28-0028 12:31
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

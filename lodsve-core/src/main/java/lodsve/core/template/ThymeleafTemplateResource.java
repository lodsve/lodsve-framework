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

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.AbstractConfigurableTemplateResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.util.Locale;
import java.util.Map;

/**
 * Thymeleaf模板引擎生成的Resource<br/>
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 14-8-30 上午1:03
 */
public class ThymeleafTemplateResource extends AbstractTemplateResource {
    private TemplateMode templateMode;

    /**
     * templateMode default is html5
     *
     * @param template
     * @param context
     */
    public ThymeleafTemplateResource(String template, Map<String, Object> context) {
        super(template, context, "resource load by Thymeleaf!");
        this.templateMode = TemplateMode.HTML;
    }

    /**
     * <p>
     * template modes defined by the {@link org.thymeleaf.templatemode.TemplateMode} class.
     * Standard template modes are:
     * </p>
     * <ul>
     * <li>HTML</li>
     * <li>XML</li>
     * <li>TEXT</li>
     * <li>JAVASCRIPT</li>
     * <li>CSS</li>
     * <li>RAW</li>
     * </ul>
     *
     * @param template
     * @param context
     * @param templateMode
     */
    public ThymeleafTemplateResource(String template, Map<String, Object> context, TemplateMode templateMode) {
        super(template, context, "resource load by Thymeleaf!");
        this.templateMode = templateMode;
    }

    @Override
    public String renderTemplate() {
        TemplateEngine templateEngine = new TemplateEngine();
        AbstractConfigurableTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setTemplateMode(this.templateMode);

        templateEngine.setTemplateResolver(templateResolver);

        return templateEngine.process(template, new Context(Locale.SIMPLIFIED_CHINESE, context));
    }
}

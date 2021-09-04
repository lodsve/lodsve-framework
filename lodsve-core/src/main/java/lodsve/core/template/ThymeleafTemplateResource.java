/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
    private final TemplateMode templateMode;

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

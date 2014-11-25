package message.template.resource;

import message.utils.StringUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

import java.util.Locale;
import java.util.Map;

/**
 * Thymeleaf模板引擎生成的Resource<br/>
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 14-8-30 上午1:03
 */
public class ThymeleafTemplateResource extends AbstractTemplateResource {
    private String templateMode;

    /**
     * templateMode default is html5
     *
     * @param template
     * @param context
     */
    public ThymeleafTemplateResource(String template, Map<String, ?> context) {
        super(template, context, "resource load by Thymeleaf!");
        this.templateMode = "html5";
    }

    /**
     * <p>
     *   template modes defined by the {@link org.thymeleaf.templatemode.StandardTemplateModeHandlers} class.
     *   Standard template modes are:
     * </p>
     * <ul>
     *   <li>XML</li>
     *   <li>VALIDXML</li>
     *   <li>XHTML</li>
     *   <li>VALIDXHTML</li>
     *   <li>HTML5</li>
     *   <li>LEGACYHTML5 (for non XML-formed HTML5 code &ndash;needs tag balancing prior to parsing)</li>
     * </ul>
     *
     * @param template
     * @param context
     * @param templateMode
     */
    public ThymeleafTemplateResource(String template, Map<String, ?> context, String templateMode) {
        super(template, context, "resource load by Thymeleaf!");
        this.templateMode = StringUtils.upperCase(templateMode);
    }

    @Override
    public String renderTemplate() {
        TemplateEngine templateEngine = new TemplateEngine();
        TemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setTemplateMode(this.templateMode);
        templateEngine.setTemplateResolver(templateResolver);

        return templateEngine.process(template, new Context(Locale.SIMPLIFIED_CHINESE, context));
    }
}

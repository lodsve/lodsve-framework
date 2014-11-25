package message.web.view;

import message.utils.StringUtils;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.InternalResourceView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import java.util.Locale;

/**
 * override UrlBasedViewResolver,add return type "jsp:" and "json:".<br/>
 * {@link org.springframework.web.servlet.view.UrlBasedViewResolver#createView(String, java.util.Locale)}
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 13-4-12 上午12:29
 */
public class GenericUrlBasedViewResolver extends UrlBasedViewResolver {
    /**
     * Prefix for special view names that specify a jsp URL
     */
    public static final String JSP_URL_PREFIX = "jsp:";

    /**
     * Prefix for special view names that specify a json URL
     */
    public static final String JSON_URL_PREFIX = "json:";

    /**
     * Overridden to implement check for "jsp:" prefix.
     * <p>Not possible in <code>loadView</code>, since overridden
     * <code>loadView</code> versions in subclasses might rely on the
     * superclass always creating instances of the required view class.
     * @see #loadView
     * @see #requiredViewClass
     */
    protected View createView(String viewName, Locale locale) throws Exception {
        // If this resolver is not supposed to handle the given view,
        // return null to pass on to the next resolver in the chain.
        if (!canHandle(viewName, locale)) {
            return null;
        }
        // Check for special "json:" prefix.
        if (viewName.startsWith(JSON_URL_PREFIX)) {
            return JSONView.view;
        }

        // Check for special "jsp:" prefix.
        if (viewName.startsWith(JSP_URL_PREFIX)) {
            String jspFile = StringUtils.remove(viewName, JSP_URL_PREFIX);
            InternalResourceView view = new InternalResourceView(jspFile);
            view.setAlwaysInclude(true);

            return view;
        }

        // Else fall back to superclass implementation: calling loadView.
        return super.createView(viewName, locale);
    }
}

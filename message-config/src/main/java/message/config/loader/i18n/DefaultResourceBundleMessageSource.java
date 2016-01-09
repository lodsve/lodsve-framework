package message.config.loader.i18n;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.util.ClassUtils;

import java.text.MessageFormat;
import java.util.*;

/**
 * 扩展的资源绑定数据源.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 13-4-17 下午9:02
 */
public class DefaultResourceBundleMessageSource extends AbstractMessageSource implements BeanClassLoaderAware {

    private ClassLoader bundleClassLoader;

    private ResourceBundleHolder resourceBundleHolder;

    private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();

    /**
     * Cache to hold loaded ResourceBundles.
     * This Map is keyed with the bundle basename, which holds a Map that is
     * keyed with the Locale and in turn holds the ResourceBundle instances.
     * This allows for very efficient hash lookups, significantly faster
     * than the ResourceBundle class's own cache.
     */
    private final Map cachedResourceBundles = new HashMap();

    /**
     * Cache to hold already generated MessageFormats.
     * This Map is keyed with the ResourceBundle, which holds a Map that is
     * keyed with the message code, which in turn holds a Map that is keyed
     * with the Locale and holds the MessageFormat values. This allows for
     * very efficient hash lookups without concatenated keys.
     *
     * @see #getMessageFormat
     */
    private final Map<ResourceBundle, Map<String, Map<Locale, MessageFormat>>> cachedBundleMessageFormats =
            new HashMap<ResourceBundle, Map<String, Map<Locale, MessageFormat>>>();

    /**
     * Set the ClassLoader to load resource bundles with.
     * <p>Default is the containing BeanFactory's
     * {@link org.springframework.beans.factory.BeanClassLoaderAware bean ClassLoader},
     * or the default ClassLoader determined by
     * {@link org.springframework.util.ClassUtils#getDefaultClassLoader()}
     * if not running within a BeanFactory.
     */
    public void setBundleClassLoader(ClassLoader classLoader) {
        this.bundleClassLoader = classLoader;
    }

    /**
     * Return the ClassLoader to load resource bundles with.
     * <p>Default is the containing BeanFactory's bean ClassLoader.
     *
     * @see #setBundleClassLoader
     */
    protected ClassLoader getBundleClassLoader() {
        return (this.bundleClassLoader != null ? this.bundleClassLoader : this.beanClassLoader);
    }

    public void setBeanClassLoader(ClassLoader classLoader) {
        this.beanClassLoader = (classLoader != null ? classLoader : ClassUtils.getDefaultClassLoader());
    }


    /**
     * Resolves the given message code as key in the registered resource bundles,
     * returning the value found in the bundle as-is (without MessageFormat parsing).
     */
    @Override
    protected String resolveCodeWithoutArguments(String code, Locale locale) {
        String result = null;

        ResourceBundle resourceBundle = getResourceBundle(locale);
        if (resourceBundle != null) {
            result = getStringOrNull(resourceBundle, code);
        }

        return result;
    }

    /**
     * Resolves the given message code as key in the registered resource bundles,
     * using a cached MessageFormat instance per message code.
     */
    @Override
    protected MessageFormat resolveCode(String code, Locale locale) {
        MessageFormat messageFormat = null;
        ResourceBundle bundle = doGetBundle(locale);
        if (bundle != null) {
            messageFormat = getMessageFormat(bundle, code, locale);
        }

        return messageFormat;
    }


    /**
     * Return a ResourceBundle for the given basename and code,
     * fetching already generated MessageFormats from the cache.
     *
     * @param locale the Locale to find the ResourceBundle for
     * @return the resulting ResourceBundle, or <code>null</code> if none
     *         found for the given basename and Locale
     */
    public ResourceBundle getResourceBundle(Locale locale) {
        synchronized (this.cachedResourceBundles) {
            ResourceBundle bundle = (ResourceBundle) this.cachedResourceBundles.get(locale);
            if (bundle != null) {
                return bundle;
            }

            bundle = doGetBundle(locale);

            if (bundle != null)
                this.cachedResourceBundles.put(locale, bundle);

            return bundle;
        }
    }

    /**
     * Obtain the resource bundle for the given Locale.
     *
     * @param locale the Locale to look for
     * @return the corresponding ResourceBundle
     * @throws java.util.MissingResourceException
     *          if no matching bundle could be found
     * @see java.util.ResourceBundle#getBundle(String, java.util.Locale, ClassLoader)
     * @see #getBundleClassLoader()
     */
    protected ResourceBundle doGetBundle(Locale locale) throws MissingResourceException {
        return resourceBundleHolder.getResourceBundle(locale);
    }

    /**
     * Return a MessageFormat for the given bundle and code,
     * fetching already generated MessageFormats from the cache.
     *
     * @param bundle the ResourceBundle to work on
     * @param code   the message code to retrieve
     * @param locale the Locale to use to build the MessageFormat
     * @return the resulting MessageFormat, or <code>null</code> if no message
     *         defined for the given code
     * @throws java.util.MissingResourceException
     *          if thrown by the ResourceBundle
     */
    protected MessageFormat getMessageFormat(ResourceBundle bundle, String code, Locale locale)
            throws MissingResourceException {

        synchronized (this.cachedBundleMessageFormats) {
            Map<String, Map<Locale, MessageFormat>> codeMap = this.cachedBundleMessageFormats.get(bundle);
            Map<Locale, MessageFormat> localeMap = null;
            if (codeMap != null) {
                localeMap = codeMap.get(code);
                if (localeMap != null) {
                    MessageFormat result = localeMap.get(locale);
                    if (result != null) {
                        return result;
                    }
                }
            }

            String msg = getStringOrNull(bundle, code);
            if (msg != null) {
                if (codeMap == null) {
                    codeMap = new HashMap<String, Map<Locale, MessageFormat>>();
                    this.cachedBundleMessageFormats.put(bundle, codeMap);
                }
                if (localeMap == null) {
                    localeMap = new HashMap<Locale, MessageFormat>();
                    codeMap.put(code, localeMap);
                }
                MessageFormat result = createMessageFormat(msg, locale);
                localeMap.put(locale, result);
                return result;
            }

            return null;
        }
    }

    private String getStringOrNull(ResourceBundle bundle, String key) {
        try {
            return bundle.getString(key);
        } catch (MissingResourceException ex) {
            // Assume key not found
            // -> do NOT throw the exception to allow for checking parent message source.
            return null;
        }
    }

    public void setResourceBundleHolder(ResourceBundleHolder resourceBundleHolder) {
        this.resourceBundleHolder = resourceBundleHolder;
    }
}

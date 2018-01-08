package lodsve.core.properties.message;

import java.util.*;

/**
 * 扩展的资源绑定.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 13-4-17 下午10:44
 */
public class DefaultResourceBundle extends ResourceBundle {

    private Map<String, Object> defaultBundleMap;
    private Map<Locale, Map<String, Object>> bundleMap;
    private Locale locale;

    public DefaultResourceBundle(Map<String, Object> defaultBundleMap, Map<Locale, Map<String, Object>> bundleMap, Locale locale) {
        this.defaultBundleMap = defaultBundleMap;
        this.bundleMap = bundleMap;
        this.locale = locale;
    }

    @Override
    protected Object handleGetObject(String key) {
        Object result;
        if (locale == null) {
            //locale为空,则取中文环境
            result = this.defaultBundleMap.get(key);
        } else {
            //从具体语言中取值
            Map<String, Object> localeMap = this.bundleMap.get(locale);
            if (localeMap == null) {
                localeMap = this.defaultBundleMap;
            }

            result = localeMap != null ? localeMap.get(key) : null;

            //取不到值,则值默认为key
            if (result == null) {
                result = key;
            }
        }

        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Enumeration<String> getKeys() {
        Set<String> keys = this.defaultBundleMap.keySet();

        return Collections.enumeration(keys);
    }
}

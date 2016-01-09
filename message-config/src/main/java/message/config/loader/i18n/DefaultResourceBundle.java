package message.config.loader.i18n;

import java.util.*;

/**
 * 扩展的资源绑定.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 13-4-17 下午10:44
 */
public class DefaultResourceBundle extends ResourceBundle {

    private Map defaultBundleMap;
    private Map bundleMap;
    private Locale locale;

    public DefaultResourceBundle(Map defaultBundleMap, Map bundleMap, Locale locale) {
        this.defaultBundleMap = defaultBundleMap;
        this.bundleMap = bundleMap;
        this.locale = locale;
    }

    protected Object handleGetObject(String key) {
        Object result;
        if (locale == null) {
            //locale为空,则取中文环境
            result = this.defaultBundleMap.get(key);
        } else {
            //从具体语言中取值
            Map localeMap = (Map) this.bundleMap.get(locale);
            if (localeMap == null)
                localeMap = this.defaultBundleMap;

            result = localeMap != null ? localeMap.get(key) : null;

            if (result == null)
                //取不到值,则值默认为key
                result = key;
        }

        return result;
    }

    public Enumeration<String> getKeys() {
        Set keys = this.defaultBundleMap.keySet();

        return Collections.enumeration(keys);
    }
}

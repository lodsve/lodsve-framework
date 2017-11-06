package lodsve.mvc.context;

import java.util.Locale;

/**
 * 语言.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 13-6-22 下午3:11
 */
public class LocaleHolder {
    private final static ThreadLocal<Locale> LOCALE_THREAD_LOCAL = new ThreadLocal<>();

    public static void setLocale(Locale locale){
        LOCALE_THREAD_LOCAL.set(locale);
    }

    public static Locale getLocale(){
        return LOCALE_THREAD_LOCAL.get();
    }

    public static void removeLocale() {
        LOCALE_THREAD_LOCAL.remove();
    }
}

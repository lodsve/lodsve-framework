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
    private static ThreadLocal<Locale> localeThreadLocal = new ThreadLocal<>();

    public static void setLocale(Locale locale){
        localeThreadLocal.set(locale);
    }

    public static Locale getLocale(){
        return localeThreadLocal.get();
    }
}

package message.utils;

import java.util.Locale;

/**
 * 设置语言.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 13-6-22 下午3:11
 */
public class LocaleHolder {
    private static ThreadLocal<Locale> LOCALE = new ThreadLocal<Locale>();

    public static void setLocale(Locale locale){
        LOCALE.set(locale);
    }

    public static Locale getLocale(){
        return LOCALE.get();
    }
}

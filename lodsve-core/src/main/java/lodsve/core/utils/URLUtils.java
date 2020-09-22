/*
 * Copyright © 2009 Sun.Hao(https://www.crazy-coder.cn/)
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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lodsve.core.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.BitSet;

/**
 * 对url的处理工具类.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2012-4-30 下午01:48:01
 */
public class URLUtils {

    /**
     * 私有化构造器
     */
    private URLUtils() {
    }

    /**
     * character which don't need encoding in url
     */
    private static final BitSet unNeedEncoding;

    static {
        unNeedEncoding = new BitSet(256);

        int i;
        /**
         * [a-z],[A-Z],[0-9] in url not need encoding
         */
        for (i = 'a'; i <= 'z'; i++) {
            unNeedEncoding.set(i);
        }
        for (i = 'A'; i <= 'Z'; i++) {
            unNeedEncoding.set(i);
        }
        for (i = '0'; i <= '9'; i++) {
            unNeedEncoding.set(i);
        }

        /**for same special letter*/
        unNeedEncoding.set(' ');
        unNeedEncoding.set('-');
        unNeedEncoding.set('_');
        unNeedEncoding.set('.');
        unNeedEncoding.set('*');

        unNeedEncoding.set('+');
        unNeedEncoding.set('%');
    }

    /**
     * 判断URL是否被encoding
     *
     * @param url url
     * @return true是    false否
     */
    public static boolean isURLEncoding(String url) {
        if (StringUtils.isEmpty(url)) {
            return false;
        }

        char[] chars = url.toCharArray();
        boolean result = false;
        for (char ch : chars) {
            if (Character.isWhitespace(ch)) {
                return false;
            }
            if (!unNeedEncoding.get(ch)) {
                return false;
            }
            if (ch == '%') {
                result = true;
            }
        }

        return result;
    }

    /**
     * encoding URL
     *
     * @param url url
     * @return
     * @throws java.io.UnsupportedEncodingException
     */
    public static String encodeURL(String url) throws UnsupportedEncodingException {
        return encodeURL(url, "UTF-8");
    }

    /**
     * encoding url with character encoding named local
     *
     * @param url   url
     * @param local character encoding
     * @return
     * @throws java.io.UnsupportedEncodingException
     */
    public static String encodeURL(String url, String local) throws UnsupportedEncodingException {
        return URLEncoder.encode(url, local);
    }

    /**
     * decoding URL
     *
     * @param url url
     * @return
     * @throws java.io.UnsupportedEncodingException
     */
    public static String decodeURL(String url) throws UnsupportedEncodingException {
        return decodeURL(url, "UTF-8");
    }

    /**
     * decoding url with character encoding named local
     *
     * @param url   url
     * @param local character encoding
     * @return
     * @throws java.io.UnsupportedEncodingException
     */
    public static String decodeURL(String url, String local) throws UnsupportedEncodingException {
        return URLDecoder.decode(url, local);
    }

}

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

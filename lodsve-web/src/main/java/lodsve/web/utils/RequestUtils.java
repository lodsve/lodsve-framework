/*
 * Copyright (C) 2018  Sun.Hao
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package lodsve.web.utils;

import lodsve.core.bean.ClientType;
import lodsve.core.bean.Constants;
import lodsve.core.utils.StringUtils;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * HttpServletRequest的工具类
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @date 2012-2-15 下午10:12:52
 */
public class RequestUtils {

    /**
     * 私有化构造器
     */
    private RequestUtils() {
    }

    /**
     * 判断是否是AJAX请求
     *
     * @param request
     * @return
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {
        String header = request.getHeader("X-Requested-With");
        return StringUtils.isNotEmpty(header) && "XMLHttpRequest".equals(header);
    }

    /**
     * 获取此次请求的url(不带contextPath)
     *
     * @param request    此次请求
     * @param needParams 是否需要参数
     * @return URL(未encoding)
     */
    public static String getRequestUrl(HttpServletRequest request, boolean needParams) {
        StringBuilder requestUrl = new StringBuilder(request.getServletPath());
        if (!needParams) {
            return requestUrl.toString();
        }

        //所带参数的MAP
        Map params = request.getParameterMap();
        Iterator it = params.keySet().iterator();
        if (it.hasNext()) {
            requestUrl.append("?");
        }
        while (it.hasNext()) {
            String key = (String) it.next();
            String[] values = (String[]) params.get(key);
            String value;
            if (values != null && values.length > 0) {
                value = values[0];
            } else {
                value = StringUtils.EMPTY;
            }

            if (StringUtils.isNotEmpty(key)) {
                //key必须不为空，value可以为空
                requestUrl.append(key).append("=").append(value).append("&");
            }
        }

        if (requestUrl.toString().endsWith("&")) {
            return requestUrl.substring(0, requestUrl.length() - 1);
        }

        return requestUrl.toString();
    }

    /**
     * 获得请求中所带的参数
     *
     * @param request
     * @return
     */
    public static List<String> getRequestParam(HttpServletRequest request) {
        List<String> urlParams = new ArrayList<>();
        Map params = request.getParameterMap();
        Iterator it = params.keySet().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            String[] values = (String[]) params.get(key);
            if (values != null && values.length > 0) {
                urlParams.add(values[0]);
            }
        }

        return urlParams;
    }

    /**
     * 获取contextPath
     *
     * @param request
     * @return
     */
    public static String getContextPath(HttpServletRequest request) {
        String contextPath = (String) request.getAttribute(Constants.DEFAULT_CONTEXT_PATH);
        if (StringUtils.isEmpty(contextPath)) {
            contextPath = request.getContextPath();

            if ((contextPath != null) && (contextPath.length() > 0)) {
                if ("/".equals(StringUtils.right(contextPath, 1))) {
                    contextPath = StringUtils.substringBeforeLast(contextPath, "/");
                }
            } else {
                contextPath = "";
            }

            request.setAttribute(Constants.DEFAULT_CONTEXT_PATH, contextPath);
        }

        return contextPath;
    }

    /**
     * get referer page from request object
     *
     * @param request HttpServletRequest object.
     * @return url of referer page.
     */
    public static String getReferer(final HttpServletRequest request) {
        String url = request.getParameter("referer");
        if (StringUtils.isBlank(url)) {
            url = request.getHeader("referer");
        }
        return url;
    }

    /**
     * 将一个对象同步到session中
     *
     * @param request request
     * @param key     key
     * @param object  对象
     */
    public static void syncToSession(HttpServletRequest request, String key, Object object) {
        HttpSession session = request.getSession();
        session.setAttribute(key, object);
    }

    /**
     * 将一个对象同步到session中
     *
     * @param request request
     * @param key     key
     * @param object  对象
     * @param life    存活时间
     */
    public static void syncToSession(HttpServletRequest request, String key, Object object, Integer life) {
        HttpSession session = request.getSession();
        session.setAttribute(key, object);
        session.setMaxInactiveInterval(life);
    }

    /**
     * 给定一段URL后面的参数,返回参数的map集合
     *
     * @param params 参数
     * @return
     */
    public static Map<String, Object> getParams(String params) {
        if (StringUtils.isEmpty(params)) {
            return Collections.emptyMap();
        }

        String[] ps = params.split("&");
        Map<String, Object> paramsMap = new HashMap<>(ps.length);
        for (String p : ps) {
            String[] tmp = p.split("=");
            if (tmp.length <= 0) {
                continue;
            }

            if (tmp.length == 1) {
                paramsMap.put(tmp[0], StringUtils.EMPTY);
            } else {
                paramsMap.put(tmp[0], tmp[1]);
            }
        }

        return paramsMap;
    }

    /**
     * 给定一段URL后面的参数和一个key,返回key对应的值
     *
     * @param params 参数
     * @param key    key
     * @return
     */
    public static String getParam(String params, String key) {
        if (StringUtils.isEmpty(params) || StringUtils.isEmpty(key)) {
            return StringUtils.EMPTY;
        }

        String[] ps = params.split("&");
        for (String p : ps) {
            String[] tmp = p.split("=");
            if (tmp.length <= 0) {
                continue;
            }

            if (key.equals(tmp[0])) {
                if (tmp.length == 1) {
                    return StringUtils.EMPTY;
                } else {
                    return tmp[1];
                }
            }
        }

        return StringUtils.EMPTY;
    }

    /**
     * 给定一段URL和一个key,返回key对应的值
     *
     * @param url url
     * @param key key
     * @return
     */
    public static String getParamFromUrl(String url, String key) {
        if (StringUtils.isEmpty(url) || StringUtils.isEmpty(key)) {
            return StringUtils.EMPTY;
        }

        if (!url.contains("?")) {
            return StringUtils.EMPTY;
        }

        String params = url.substring(url.indexOf("?") + 1);
        return getParam(params, key);
    }

    public static HttpServletRequest getCurrentRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        Assert.state(requestAttributes != null, "Could not find current request via RequestContextHolder");
        Assert.isInstanceOf(ServletRequestAttributes.class, requestAttributes);
        Assert.notNull(requestAttributes);
        HttpServletRequest servletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
        Assert.state(servletRequest != null, "Could not find current HttpServletRequest");
        return servletRequest;
    }

    public static String getUserAgent(HttpServletRequest request) {
        return StringUtils.defaultString(request.getHeader("user-agent")).toLowerCase();
    }

    public static ClientType getClientType() {
        HttpServletRequest request = getCurrentRequest();
        if (request != null) {
            String userAgent = getUserAgent(request);
            if (userAgent.lastIndexOf("mah/") > -1 || userAgent.lastIndexOf("thttpclient") > -1) {
                return ClientType.APP;
            } else if (userAgent.lastIndexOf("micromessenger") > -1) {
                return ClientType.WEIXIN;
            } else if (userAgent.lastIndexOf("alipayclient") > -1) {
                return ClientType.ALIPAY;
            } else if (userAgent.lastIndexOf("qq") > -1) {
                return ClientType.QQ;
            } else {
                return ClientType.BROWSER;
            }
        }
        return ClientType.UNKNOWN;
    }
}

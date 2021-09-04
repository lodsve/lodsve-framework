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
package lodsve.web.utils;

import lodsve.core.json.JsonConverterFactory;
import lodsve.core.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.*;

/**
 * 操作ip的工具类.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 13-12-10 下午11:37
 */
public class IpUtils {
    private static final Logger logger = LoggerFactory.getLogger(IpUtils.class);

    /**
     * 默认的识别IP的地址(第三方运营商)
     */
    private static final String REQUEST_URL = "http://ip.taobao.com/service/getIpInfo.php?ip=%s";

    /**
     * 私有化构造器
     */
    private IpUtils() {
    }

    /**
     * 根据给定IP获取IP地址的全部信息<br/>
     * eg:<br/>
     * give ip 222.94.109.17,you will receive a map.<br/>
     * map is {"region":"江苏省","area_id":"300000","country_id":"CN","isp":"电信","region_id":"320000","country":"中国","city":"南京市","isp_id":"100017","ip":"222.94.109.17","city_id":"320100","area":"华东","county":"","county_id":"-1"}
     *
     * @param ip ip
     * @return IP地址的全部信息
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> getAllInfo(String ip) {
        if (StringUtils.isEmpty(ip)) {
            logger.error("ip is null!!!");
            return Collections.emptyMap();
        }

        String message;
        try {
            message = HttpUtils.get(String.format(REQUEST_URL, ip));
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return Collections.emptyMap();
        }

        Map<String, Object> object = JsonConverterFactory.getConverter(JsonConverterFactory.JsonMode.JACKSON).toMap(message);

        Object result = object.get("code");
        if (result != null && "0".equals(result.toString())) {
            logger.debug("get from '{}' success!", REQUEST_URL);
            return (Map<String, String>) object.get("data");
        } else {
            logger.error("get from '{}' failure!", REQUEST_URL);
            return Collections.emptyMap();
        }
    }

    /**
     * GET The Country of given IP!
     *
     * @param ip ip
     * @return Country
     */
    public static String getCountry(String ip) {
        return get(ip, IpKey.COUNTRY);
    }

    /**
     * GET The Area of given IP!
     *
     * @param ip ip
     * @return Area
     */
    public static String getArea(String ip) {
        return get(ip, IpKey.AREA);
    }

    /**
     * GET The Region of given IP!
     *
     * @param ip ip
     * @return Region
     */
    public static String getRegion(String ip) {
        return get(ip, IpKey.REGION);
    }

    /**
     * GET The City of given IP!
     *
     * @param ip ip
     * @return City
     */
    public static String getCity(String ip) {
        return get(ip, IpKey.CITY);
    }

    /**
     * GET The Isp of given IP!
     *
     * @param ip ip
     * @return Isp
     */
    public static String getIsp(String ip) {
        return get(ip, IpKey.ISP);
    }

    /**
     * GET The County of given IP!
     *
     * @param ip ip
     * @return County
     */
    public static String getCounty(String ip) {
        return get(ip, IpKey.COUNTY);
    }

    /**
     * 获取给定IP的一些信息
     *
     * @param ip  ip
     * @param key IpKey中的值
     * @return 信息
     */
    public static String get(String ip, IpKey key) {
        Map<String, String> allInfo = getAllInfo(ip);

        if (allInfo != null && !allInfo.isEmpty()) {
            return allInfo.get(key.toString().toLowerCase());
        }

        return StringUtils.EMPTY;
    }

    /**
     * 获取系统中第一个IP不为127.0.0.1的网卡的ip地址
     *
     * @return ip地址
     */
    public static String getInetIp() {
        List<String> ips = getInetIps();
        for (String ip : ips) {
            if (!"127.0.0.1".equals(ip)) {
                return ip;
            }
        }

        return "127.0.0.1";
    }

    /**
     * 获取系统中所有网卡的ip地址
     *
     * @return ip地址
     */
    public static List<String> getInetIps() {
        List<String> ipList = new LinkedList<>();
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            NetworkInterface networkInterface;
            Enumeration<InetAddress> inetAddresses;
            InetAddress inetAddress;
            String ip;
            while (networkInterfaces.hasMoreElements()) {
                networkInterface = networkInterfaces.nextElement();
                inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    inetAddress = inetAddresses.nextElement();
                    if (inetAddress instanceof Inet4Address) {
                        ip = inetAddress.getHostAddress();
                        ipList.add(ip);
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return ipList;
    }

    public enum IpKey {
        /**
         * 国家/国家ID
         */
        COUNTRY, COUNTRY_ID,
        /**
         * 地区/地区ID
         */
        AREA, AREA_ID,
        /**
         * 省份/省份ID
         */
        REGION, REGION_ID,
        /**
         * 城市/城市ID
         */
        CITY, CITY_ID,
        /**
         * 县/县ID
         */
        COUNTY, COUNTY_ID,
        /**
         * 网络运营商/网络运营商ID
         */
        ISP, ISP_ID
    }
}

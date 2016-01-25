package message.base.utils;

import org.apache.commons.httpclient.NameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

/**
 * 操作ip的工具类.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0 13-12-10 下午11:37
 */
public class IpUtils {
    private static final Logger logger = LoggerFactory.getLogger(IpUtils.class);

    /**
     * 默认的识别IP的地址(第三方运营商)
     */
    private static final String REQUEST_URL = "http://ip.taobao.com/service/getIpInfo.php";
    /**
     * url中的参数key
     */
    private static final String IP_KEY = "ip";

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
     * @return
     * @throws java.io.IOException
     */
    public static Map<String, String> getAllInfo(String ip) {
        if (StringUtils.isEmpty(ip)) {
            logger.error("ip is null!!!");
            return Collections.EMPTY_MAP;
        }

        NameValuePair nameValuePair = new NameValuePair(IP_KEY, ip);
        String message = null;
        try {
            message = HttpClientUtils.get(REQUEST_URL, new NameValuePair[]{nameValuePair});
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return Collections.EMPTY_MAP;
        }

        return null;
        // TODO
//        JSONObject object = JSONObject.fromObject(message);
//        Integer result = object.getInt("code");
//        if(result != null && Integer.valueOf(0).equals(result)){
//            logger.debug("get from '{}' success!", REQUEST_URL);
//            return (Map<String, String>) object.get("data");
//        }else{
//            logger.error("get from '{}' failure!", REQUEST_URL);
//            return Collections.EMPTY_MAP;
//        }
    }

    /**
     * GET The Country of given IP!
     *
     * @param ip ip
     * @return
     */
    public static String getCountry(String ip) {
        return get(ip, IpKeys.COUNTRY);
    }

    /**
     * GET The Area of given IP!
     *
     * @param ip ip
     * @return
     */
    public static String getArea(String ip) {
        return get(ip, IpKeys.AREA);
    }

    /**
     * GET The Region of given IP!
     *
     * @param ip ip
     * @return
     */
    public static String getRegion(String ip) {
        return get(ip, IpKeys.REGION);
    }

    /**
     * GET The City of given IP!
     *
     * @param ip ip
     * @return
     */
    public static String getCity(String ip) {
        return get(ip, IpKeys.CITY);
    }

    /**
     * GET The Isp of given IP!
     *
     * @param ip ip
     * @return
     */
    public static String getIsp(String ip) {
        return get(ip, IpKeys.ISP);
    }

    /**
     * GET The County of given IP!
     *
     * @param ip ip
     * @return
     */
    public static String getCounty(String ip) {
        return get(ip, IpKeys.COUNTY);
    }

    /**
     * 获取给定IP的一些信息
     *
     * @param ip  ip
     * @param key IpKeys中的值
     * @return
     */
    public static String get(String ip, IpKeys key) {
        Map<String, String> allInfo = getAllInfo(ip);

        if (allInfo != null && !allInfo.isEmpty()) {
            return allInfo.get(key.toString().toLowerCase());
        }

        return StringUtils.EMPTY;
    }

    public enum IpKeys {
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

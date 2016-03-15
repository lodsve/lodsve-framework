package message.base.utils;

import com.alibaba.fastjson.JSONObject;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final String REQUEST_URL = "http://ip.taobao.com/base/getIpInfo.php?ip=%s";

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
            return Collections.emptyMap();
        }

        String message;
        try {
            message = HttpClientUtils.get(String.format(REQUEST_URL, ip));
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return Collections.emptyMap();
        }

        JSONObject object = JSONObject.parseObject(message);
        Integer result = object.getInteger("code");
        if (result != null && Integer.valueOf(0).equals(result)) {
            logger.debug("get from '{}' success!", REQUEST_URL);
            return (Map<String, String>) object.getObject("data", Map.class);
        } else {
            logger.error("get from '{}' failure!", REQUEST_URL);
            return Collections.emptyMap();
        }
    }

    /**
     * GET The Country of given IP!
     *
     * @param ip ip
     * @return
     */
    public static String getCountry(String ip) {
        return get(ip, IpKey.COUNTRY);
    }

    /**
     * GET The Area of given IP!
     *
     * @param ip ip
     * @return
     */
    public static String getArea(String ip) {
        return get(ip, IpKey.AREA);
    }

    /**
     * GET The Region of given IP!
     *
     * @param ip ip
     * @return
     */
    public static String getRegion(String ip) {
        return get(ip, IpKey.REGION);
    }

    /**
     * GET The City of given IP!
     *
     * @param ip ip
     * @return
     */
    public static String getCity(String ip) {
        return get(ip, IpKey.CITY);
    }

    /**
     * GET The Isp of given IP!
     *
     * @param ip ip
     * @return
     */
    public static String getIsp(String ip) {
        return get(ip, IpKey.ISP);
    }

    /**
     * GET The County of given IP!
     *
     * @param ip ip
     * @return
     */
    public static String getCounty(String ip) {
        return get(ip, IpKey.COUNTY);
    }

    /**
     * 获取给定IP的一些信息
     *
     * @param ip  ip
     * @param key IpKey中的值
     * @return
     */
    public static String get(String ip, IpKey key) {
        Map<String, String> allInfo = getAllInfo(ip);

        if (allInfo != null && !allInfo.isEmpty()) {
            return allInfo.get(key.toString().toLowerCase());
        }

        return StringUtils.EMPTY;
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

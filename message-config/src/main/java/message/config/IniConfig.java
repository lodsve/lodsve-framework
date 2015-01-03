package message.config;

import message.config.exception.ConfigException;
import message.config.ini.IniLoader;
import message.utils.StringUtils;

import java.util.Map;
import java.util.Set;

/**
 * ini配置获取.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 2014-12-31 14:19
 */
public class IniConfig {
    private static Map<String, Map<String, String>> inis;

    static {
        init();
    }

    private static void init(){
        try {
            inis = IniLoader.getIni();
        } catch (Exception e) {
            throw new ConfigException(10008, "加载配置文件发生IO异常");
        }
    }

    /**
     * 获取keys
     *
     * @return
     */
    public static Set<String> getSections(){
        return inis.keySet();
    }

    /**
     * 根据section获取配置
     *
     * @param section
     * @return
     */
    public static Map<String, String> getConfigsBySection(String section){
        return inis.get(section);
    }

    /**
     * 获取配置
     *
     * @param section
     * @param key
     * @return
     */
    public static String getConfig(String section, String key){
        Map<String, String> sections = inis.get(section);
        if(sections == null || sections.isEmpty()) {
            return StringUtils.EMPTY;
        }

        return sections.get(key);
    }

    /**
     * 获取布尔型配置
     *
     * @param section
     * @param key
     * @return
     */
    public static boolean getBooleanConfig(String section, String key){
        String config = getConfig(section, key);

        return "true".equals(config) || "1".equals(config) || "y".equalsIgnoreCase(config) || "yes".equalsIgnoreCase(config);
    }

    /**
     * 获取所有配置
     *
     * @return
     */
    public static Map<String, Map<String, String>> getAllConfigs(){
        return inis;
    }
}

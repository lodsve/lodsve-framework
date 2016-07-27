package lodsve.core.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import lodsve.core.config.loader.ini.IniLoader;
import lodsve.core.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

/**
 * ini配置获取.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 2014-12-31 14:19
 */
public class IniConfig {
    private static final Logger logger = LoggerFactory.getLogger(IniConfig.class);
    private static Map<String, Map<String, String>> inis;

    static {
        init();
    }

    private static void init() {
        try {
            inis = IniLoader.getIni();
        } catch (Exception e) {
            throw new RuntimeException("加载配置文件发生IO异常");
        }
    }

    /**
     * 获取keys
     *
     * @return
     */
    public static Set<String> getSections() {
        return inis.keySet();
    }

    /**
     * 根据section获取配置
     *
     * @param section
     * @return
     */
    public static Map<String, String> getConfigsBySection(String section) {
        return inis.get(section);
    }

    /**
     * 获取配置
     *
     * @param section
     * @param key
     * @return
     */
    public static String getConfig(String section, String key) {
        Map<String, String> sections = inis.get(section);
        if (sections == null || sections.isEmpty()) {
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
    public static boolean getBooleanConfig(String section, String key) {
        String config = getConfig(section, key);

        return "true".equals(config) || "1".equals(config) || "y".equalsIgnoreCase(config) || "yes".equalsIgnoreCase(config);
    }

    /**
     * 获取所有配置
     *
     * @return
     */
    public static Map<String, Map<String, String>> getAllConfigs() {
        return inis;
    }

    /**
     * 获取指定ini资源
     *
     * @param resource
     * @return
     */
    public static Map<String, Map<String, String>> getFileConfig(Resource resource) {
        Assert.notNull(resource, "资源不能为空！");

        Map<String, Map<String, String>> maps = new HashMap<String, Map<String, String>>();
        try {
            maps.putAll(IniLoader.getIni(resource));
        } catch (Exception e) {
            logger.error("加载资源'{}'失败！", resource);
            logger.error(e.getMessage(), e);
        }

        return maps;
    }
}

package lodsve.core.properties.message;

import lodsve.core.utils.FileUtils;
import lodsve.core.utils.ResourceUtils;
import lodsve.core.utils.StringUtils;
import org.apache.commons.lang.LocaleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * 存放消息源的容器.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 13-4-15 下午11:11
 */
public class ResourceBundleHolder implements Serializable {
    /**
     * for logger
     */
    private static final Logger logger = LoggerFactory.getLogger(ResourceBundleHolder.class);

    private static final String SUFFIX_OF_PROPERTIES = ".properties";
    private static final String SUFFIX_OF_TEXT = ".txt";
    private static final String SUFFIX_OF_HTML = ".html";

    /**
     * 存放资源名称与值，不包含locale信息.
     */
    private Map defaultBundleMap = new HashMap();

    /**
     * 存放每个locale对应的资源信息.
     */
    private Map<Locale, Map> bundledMap = new HashMap<>();

    public void loadMessageResource(Resource resource) {
        if (!resource.exists() || !resource.isReadable()) {
            logger.debug("give file '{}' not exist or can not read!", resource);
            return;
        }

        String fileName = resource.getFilename();

        Locale locale = null;
        try {
            locale = this.getLocaleFromFileName(fileName);
        } catch (Exception e) {
            if (logger.isWarnEnabled()) {
                logger.warn("file '{}' can not get any locale, continue!", resource);
            }
        }

        // baseName 就是去掉语言信息的部分
        String baseName = null == locale ? fileName : StringUtils.replace(fileName, "_" + locale.toString(), "");
        baseName = FileUtils.getFileName(baseName);
        Properties properties = this.getPropertiesFromFile(baseName, resource);

        if (properties != null) {
            this.addProperties(properties, locale);
        } else {
            logger.warn("can not get any properties from given file '{}'!", resource);
        }
    }

    private Properties getPropertiesFromFile(String baseName, Resource resource) {
        Properties properties = new Properties();

        String fileType = FileUtils.getFileExt(resource.getFilename());

        try {
            if (SUFFIX_OF_PROPERTIES.equals(fileType)) {
                //1.properties
                PropertiesLoaderUtils.fillProperties(properties, new EncodedResource(resource, "UTF-8"));
            } else if (SUFFIX_OF_TEXT.equals(fileType) || SUFFIX_OF_HTML.equals(fileType)) {
                //2.txt/html
                properties.put(baseName, ResourceUtils.getContent(resource));
            } else {
                //do nothing!
                logger.debug("this file '{}' is not properties, txt, html!", resource);
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return null;
        }

        return properties;
    }

    private Locale getLocaleFromFileName(String fileName) {
        if (StringUtils.indexOf(fileName, "_") == -1) {
            return null;
        }

        String ext = FileUtils.getFileExt(fileName);
        String fileNameWithoutExt = StringUtils.substringBefore(fileName, ext);

        try {
            return LocaleUtils.toLocale(StringUtils.substring(fileNameWithoutExt, fileNameWithoutExt.length() - 5, fileNameWithoutExt.length()));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将文件中的资源放入map中
     *
     * @param src
     * @param locale
     */
    @SuppressWarnings("unchecked")
    private void addProperties(Map src, Locale locale) {
        if (locale == null) {
            mergePropertiesToMap(defaultBundleMap, src);
            locale = Locale.CHINA;
        }

        Map propertiesMap = this.bundledMap.get(locale);

        //各种语言的property存储map
        if (propertiesMap == null) {
            propertiesMap = new HashMap(16);
            this.bundledMap.put(locale, propertiesMap);
        }

        //对各种语言的property进行处理
        mergePropertiesToMap(propertiesMap, src);
    }

    /**
     * 将所给的资源Map融合到目标Map中.
     *
     * @param destMap 目标Map.
     * @param src     资源.
     */
    private void mergePropertiesToMap(Map<String, Object> destMap, Map src) {
        for (Object keyObj : src.keySet()) {
            String key = keyObj.toString();

            if (!destMap.containsKey(key)) {
                //不存在的话就直接添加
                destMap.put(key, src.get(key));
            }
        }
    }

    public ResourceBundle getResourceBundle(Locale locale) {
        return new DefaultResourceBundle(this.defaultBundleMap, this.bundledMap, locale);
    }
}

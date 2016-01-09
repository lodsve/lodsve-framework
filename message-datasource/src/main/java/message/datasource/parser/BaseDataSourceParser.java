package message.datasource.parser;

import message.config.SystemConfig;
import message.config.loader.properties.Configuration;

/**
 * 数据源配置的公共方法.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/6/24 下午12:44
 */
public class BaseDataSourceParser {
    private static final String DATASOURCE_FILE_NAME = "dataSource.properties";
    protected static final String DATASOURCE_ELE_NAME = "name";
    protected static Configuration configuration;

    static {
        configuration = SystemConfig.getFileConfiguration(DATASOURCE_FILE_NAME);
    }
}

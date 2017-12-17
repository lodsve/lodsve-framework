package lodsve.search.configs;

import lodsve.core.autoconfigure.annotations.ConfigurationProperties;
import org.springframework.core.io.Resource;

/**
 * 搜索的配置.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2016/1/20 12:39
 */
@ConfigurationProperties(prefix = "lodsve.search", locations = "file:${params.root}/framework/search.properties")
public class SearchProperties {
    /**
     * lucene索引文件路径
     */
    private String index;
    /**
     * solr服务器地址
     */
    private String server;
    /**
     * 庖丁解词的配置文件位置
     */
    private Resource paodingAnalyzerFile;
    /**
     * 高亮前缀
     */
    private String prefix = "<span style='color: red'>";
    /**
     * 高亮后缀
     */
    private String suffix = "</span>";

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public Resource getPaodingAnalyzerFile() {
        return paodingAnalyzerFile;
    }

    public void setPaodingAnalyzerFile(Resource paodingAnalyzerFile) {
        this.paodingAnalyzerFile = paodingAnalyzerFile;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}

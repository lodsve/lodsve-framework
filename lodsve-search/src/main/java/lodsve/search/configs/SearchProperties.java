package lodsve.search.configs;

import lodsve.core.autoconfigure.annotations.ConfigurationProperties;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

/**
 * 搜索的配置.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 2016/1/20 12:39
 */
@ConfigurationProperties(prefix = "lodsve.search", locations = "file:${params.root}/framework/search.properties")
public class SearchProperties {
    private Solr solr;
    private Lucene lucene;

    public Solr getSolr() {
        return solr;
    }

    public void setSolr(Solr solr) {
        this.solr = solr;
    }

    public Lucene getLucene() {
        return lucene;
    }

    public void setLucene(Lucene lucene) {
        this.lucene = lucene;
    }

    public static class Solr extends Common {
        /**
         * solr服务器地址
         */
        private String server;
        /**
         * solr 6.6.0使用的哪个core
         */
        private String core;

        public String getServer() {
            return server;
        }

        public void setServer(String server) {
            this.server = server;
        }

        public String getCore() {
            return core;
        }

        public void setCore(String core) {
            this.core = core;
        }
    }

    public static class Lucene extends Common {
        /**
         * lucene索引文件路径
         */
        private String index;
        private Class<?> analyzer = StandardAnalyzer.class;

        public String getIndex() {
            return index;
        }

        public void setIndex(String index) {
            this.index = index;
        }

        public Class<?> getAnalyzer() {
            return analyzer;
        }

        public void setAnalyzer(Class<?> analyzer) {
            this.analyzer = analyzer;
        }
    }

    public static class Common {
        /**
         * 高亮前缀
         */
        private String prefix = "<span style='color: red'>";
        /**
         * 高亮后缀
         */
        private String suffix = "</span>";

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
}

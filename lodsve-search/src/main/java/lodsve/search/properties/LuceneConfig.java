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

package lodsve.search.properties;

import org.apache.lucene.analysis.standard.StandardAnalyzer;

/**
 * Lucene Config.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @date 2018-4-25-0025 14:47
 */
public class LuceneConfig {
    /**
     * 高亮前缀
     */
    private String prefix = "<span style='color: red'>";
    /**
     * 高亮后缀
     */
    private String suffix = "</span>";
    /**
     * lucene索引文件路径
     */
    private String index;
    private Class<?> analyzer = StandardAnalyzer.class;

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

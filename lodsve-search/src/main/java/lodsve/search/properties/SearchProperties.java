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

import lodsve.core.properties.relaxedbind.annotations.ConfigurationProperties;

/**
 * 搜索的配置.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @date 2016/1/20 12:39
 */
@ConfigurationProperties(prefix = "lodsve.search", locations = "${params.root}/framework/search.properties")
public class SearchProperties {
    private SolrConfig solr;
    private LuceneConfig lucene;

    public SolrConfig getSolr() {
        return solr;
    }

    public void setSolr(SolrConfig solr) {
        this.solr = solr;
    }

    public LuceneConfig getLucene() {
        return lucene;
    }

    public void setLucene(LuceneConfig lucene) {
        this.lucene = lucene;
    }
}

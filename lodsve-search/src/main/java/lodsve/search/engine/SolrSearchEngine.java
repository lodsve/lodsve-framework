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

package lodsve.search.engine;

import lodsve.core.utils.StringUtils;
import lodsve.search.bean.BaseSearchBean;
import lodsve.search.exception.SolrException;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基于solr实现的搜索引擎.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 13-5-5 下午9:36
 */
public class SolrSearchEngine extends AbstractSearchEngine {
    private static final Logger logger = LoggerFactory.getLogger(SolrSearchEngine.class);
    private HttpSolrClient solrClient;

    public SolrSearchEngine(HttpSolrClient solrClient, String htmlPrefix, String htmlSuffix) {
        super.setHtmlPrefix(htmlPrefix);
        super.setHtmlSuffix(htmlSuffix);

        this.solrClient = solrClient;
    }

    @Override
    public synchronized void doIndex(List<BaseSearchBean> baseSearchBeans) throws Exception {
        List<SolrInputDocument> sids = new ArrayList<>();
        for (BaseSearchBean sb : baseSearchBeans) {
            if (sb == null) {
                logger.debug("give BaseSearchBean is null!");
                return;
            }

            //初始化一些字段
            sb.initPublicFields();
            SolrInputDocument sid = new SolrInputDocument();

            //保证每个对象的唯一性,而且通过对象的主键可以明确的找到这个对象在solr中的索引
            sid.addField("id", "uniqueKey-" + sb.getIndexType() + "-" + sb.getId());
            if (StringUtils.isEmpty(sb.getId())) {
                throw new SolrException(104003, "you must give a id");
            }
            sid.addField("pkId", sb.getId());

            if (StringUtils.isEmpty(sb.getKeyword())) {
                throw new SolrException(104004, "you must give a keyword");
            }
            sid.addField("keyword", sb.getKeyword());

            if (StringUtils.isEmpty(sb.getOwerId())) {
                throw new SolrException(104005, "you must give a owerId");
            }
            sid.addField("owerId", sb.getOwerId());

            if (StringUtils.isEmpty(sb.getOwerName())) {
                throw new SolrException(104006, "you must give a owerName");
            }
            sid.addField("owerName", sb.getOwerName());

            if (StringUtils.isEmpty(sb.getLink())) {
                throw new SolrException(104007, "you must give a link");
            }
            sid.addField("link", sb.getLink());

            if (StringUtils.isEmpty(sb.getCreateDate())) {
                throw new SolrException(104008, "you must give a createDate");
            }
            sid.addField("createDate", sb.getCreateDate());

            sid.addField("indexType", getIndexType(sb));

            String[] doIndexFields = sb.getDoIndexFields();
            Map<String, String> values = sb.getIndexFieldValues();
            if (doIndexFields != null && doIndexFields.length > 0) {
                for (String f : doIndexFields) {
                    //匹配动态字段
                    sid.addField(f + "_lodsve", values.get(f));
                }
            }

            sids.add(sid);
        }

        UpdateResponse response = solrClient.add(sids);
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("Add doc size '%d', result: '%d', Qtime: '%d'", sids.size(), response.getStatus(), response.getQTime()));
        }
        UpdateResponse commit = solrClient.commit();
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("commit doc to index result: '%d' Qtime: '%d'", commit.getStatus(), commit.getQTime()));
        }
    }

    @Override
    public synchronized void deleteIndex(BaseSearchBean bean) throws Exception {
        if (bean == null) {
            logger.warn("Get search bean is empty!");
            return;
        }

        String id = bean.getId();

        if (StringUtils.isEmpty(id)) {
            logger.warn("get id and id value from bean is empty!");
            return;
        }

        UpdateResponse response = solrClient.deleteById(id);
        solrClient.commit();

        if (logger.isDebugEnabled()) {
            logger.debug(String.format("delete id '%s', result: '%d' Qtime: '%d'", id, response.getStatus(), response.getQTime()));
        }
    }

    @Override
    public synchronized void deleteIndexs(List<BaseSearchBean> beans) throws Exception {
        if (beans == null) {
            logger.warn("Get beans is empty!");
            return;
        }

        for (BaseSearchBean bean : beans) {
            this.deleteIndex(bean);
        }
    }

    @Override
    public Page<BaseSearchBean> doSearch(List<BaseSearchBean> beans, boolean isHighlighter, Pageable pageable) throws Exception {
        if (beans == null || beans.isEmpty()) {
            logger.debug("given search beans is empty!");
            return new PageImpl<>(Collections.<BaseSearchBean>emptyList(), null, 0);
        }

        List<BaseSearchBean> queryResults = new ArrayList<>();

        StringBuilder querySB = new StringBuilder();
        for (BaseSearchBean bean : beans) {
            //要进行检索的字段
            String[] doSearchFields = bean.getDoSearchFields();
            if (doSearchFields == null || doSearchFields.length == 0) {
                continue;
            }

            for (int i = 0; i < doSearchFields.length; i++) {
                String f = doSearchFields[i];
                querySB.append("(").append(f).append("_lodsve:*").append(bean.getKeyword()).append("*").append(")");

                if (i + 1 != doSearchFields.length) {
                    querySB.append(" OR ");
                }
            }
        }

        if (StringUtils.isEmpty(querySB.toString())) {
            logger.warn("query string is null!");
            return new PageImpl<>(Collections.<BaseSearchBean>emptyList(), null, 0);
        }

        SolrQuery query = new SolrQuery();
        query.setQuery(querySB.toString());
        query.setStart(pageable.getPageNumber() * pageable.getPageSize());
        query.setRows(pageable.getPageSize());
        query.setFields("*", "score");

        if (isHighlighter) {
            query.setHighlight(true).setHighlightSimplePre(getHtmlPrefix()).setHighlightSimplePost(getHtmlSuffix());
            query.setHighlightSnippets(2);
            query.setHighlightFragsize(1000);
            query.setParam("hl.fl", "*");
        }

        QueryResponse response = solrClient.query(query);
        SolrDocumentList sd = response.getResults();

        for (SolrDocument doc : sd) {
            String indexType = doc.get("indexType").toString();
            BaseSearchBean result = super.getBaseSearchBean(indexType, beans);

            try {
                result.setId(doc.getFieldValue("pkId").toString());
                result.setLink(doc.getFieldValue("link").toString());
                result.setOwerId(doc.getFieldValue("owerId").toString());
                result.setOwerName(doc.getFieldValue("owerName").toString());
                result.setCreateDate(doc.getFieldValue("createDate").toString());
                result.setIndexType(doc.getFieldValue("indexType").toString());

                String keyword = StringUtils.EMPTY;
                if (isHighlighter) {
                    String id = (String) doc.getFieldValue("id");
                    List temp = response.getHighlighting().get(id).get("keyword");
                    if (temp != null && !temp.isEmpty()) {
                        keyword = temp.get(0).toString();
                    }
                }

                if (StringUtils.isEmpty(keyword)) {
                    keyword = doc.getFieldValue("keyword").toString();
                }
                result.setKeyword(keyword);

                //要进行检索的字段
                String[] doSearchFields = result.getDoSearchFields();
                if (doSearchFields == null || doSearchFields.length == 0) {
                    continue;
                }
                Map<String, String> extendValues = new HashMap<>(doSearchFields.length);
                for (String field : doSearchFields) {
                    String value = doc.getFieldValue(field + "_lodsve").toString();
                    if (isHighlighter) {
                        String id = (String) doc.getFieldValue("id");
                        List temp = response.getHighlighting().get(id).get(field + "_lodsve");
                        if (temp != null && !temp.isEmpty()) {
                            value = temp.get(0).toString();
                        }
                    }

                    extendValues.put(field, value);
                }

                result.setSearchValues(extendValues);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }

            queryResults.add(result);
        }

        return new PageImpl<>(queryResults, pageable, sd.getNumFound());
    }

    @Override
    public synchronized void deleteIndexsByIndexType(Class<? extends BaseSearchBean> clazz) throws Exception {
        String indexType = getIndexType(BeanUtils.instantiate(clazz));
        this.deleteIndexsByIndexType(indexType);
    }

    @Override
    public synchronized void deleteIndexsByIndexType(String indexType) throws Exception {
        UpdateResponse ur = solrClient.deleteByQuery("indexType:" + indexType);
        logger.debug("delete all indexs! UpdateResponse is '{}'! execute for '{}'ms!", ur, ur.getElapsedTime());
        solrClient.commit();
    }

    @Override
    public synchronized void deleteAllIndexs() throws Exception {
        UpdateResponse ur = solrClient.deleteByQuery("*:*");
        logger.debug("delete all indexs! UpdateResponse is '{}'! execute for '{}'ms!", ur, ur.getElapsedTime());
        solrClient.commit();
    }

    @Override
    public void updateIndex(BaseSearchBean baseSearchBeans) throws Exception {
        this.updateIndexs(Collections.singletonList(baseSearchBeans));
    }

    /**
     * 更新索引<br/>
     * 在solr中更新索引也就是创建索引(当有相同ID存在的时候,仅仅更新,否则新建)<br/>
     * {@link lodsve.search.engine.SolrSearchEngine#doIndex(java.util.List)}
     *
     * @param baseSearchBeans 需要更新的beans
     * @throws Exception
     */
    @Override
    public void updateIndexs(List<BaseSearchBean> baseSearchBeans) throws Exception {
        this.doIndex(baseSearchBeans);
    }
}

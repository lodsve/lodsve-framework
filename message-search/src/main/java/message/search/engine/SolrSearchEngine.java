package message.search.engine;

import message.base.pagination.PaginationSupport;
import message.base.pagination.PaginationUtils;
import message.search.SearchBean;
import message.search.SearchInitException;
import message.utils.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.io.File;
import java.net.MalformedURLException;
import java.util.*;

/**
 * 基于solr实现的搜索引擎.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 13-5-5 下午9:36
 */
public class SolrSearchEngine extends AbstractSearchEngine {
    private static final Logger logger = LoggerFactory.getLogger(SolrSearchEngine.class);
    private String server = "http://localhost:8080/solr";
    private static SolrServer solrServer = null;

    private SolrServer getSolrServer(){
        if(StringUtils.isEmpty(server)){
            logger.error("null solr server path!");
            throw new SearchInitException(10004, "Give a null solr server path");
        }

        try {
            if(solrServer == null)
                solrServer = new CommonsHttpSolrServer(server);

            return solrServer;
        } catch (MalformedURLException e) {
            throw new SearchInitException(10004, "Connect to solr server error use server '" + server + "'");
        }
    }

    public synchronized void doIndex(List<SearchBean> searchBeans) throws Exception {
        SolrServer solrServer = getSolrServer();
        List<SolrInputDocument> sids = new ArrayList<SolrInputDocument>();
        for(SearchBean sb : searchBeans){
            if(sb == null){
                logger.debug("give SearchBean is null!");
                return;
            }

            //初始化一些字段
            sb.initPublicFields();
            SolrInputDocument sid = new SolrInputDocument();

            //保证每个对象的唯一性,而且通过对象的主键可以明确的找到这个对象在solr中的索引
            sid.addField("id", "uniqueKey-" + sb.getIndexType() + "-" + sb.getId());
            if(StringUtils.isEmpty(sb.getId())){
                throw new SearchInitException(10003, "you must give a id");
            }
            sid.addField("pkId", sb.getId());

            if(StringUtils.isEmpty(sb.getKeyword())){
                throw new SearchInitException(10003, "you must give a keyword");
            }
            sid.addField("keyword", sb.getKeyword());

            if(StringUtils.isEmpty(sb.getOwerId())){
                throw new SearchInitException(10003, "you must give a owerId");
            }
            sid.addField("owerId", sb.getOwerId());

            if(StringUtils.isEmpty(sb.getOwerName())){
                throw new SearchInitException(10003, "you must give a owerName");
            }
            sid.addField("owerName", sb.getOwerName());

            if(StringUtils.isEmpty(sb.getLink())){
                throw new SearchInitException(10003, "you must give a link");
            }
            sid.addField("link", sb.getLink());

            if(StringUtils.isEmpty(sb.getCreateDate())){
                throw new SearchInitException(10003, "you must give a createDate");
            }
            sid.addField("createDate", sb.getCreateDate());

            sid.addField("indexType", getIndexType(sb));

            String[] doIndexFields = sb.getDoIndexFields();
            Map<String, String> values = sb.getIndexFieldValues();
            if(doIndexFields != null && doIndexFields.length > 0){
                for(String f : doIndexFields){
                    //匹配动态字段
                    sid.addField(f + "_message", values.get(f));
                }
            }

            //进行索引的文件字段
            Map<String, File> files = sb.getFileMap();
            if(files != null && !files.isEmpty()) {
                for(Iterator<Map.Entry<String, File>> i = files.entrySet().iterator(); i.hasNext(); ){
                    Map.Entry<String, File> e = i.next();
                    String column = e.getKey();
                    File file = e.getValue();

                    if(!Arrays.asList(doIndexFields).contains(column))  continue;

                    String content = getFileContent(file);

                    //匹配动态字段
                    sid.addField(column + "_message", content);
                }
            }

            sids.add(sid);
        }

        solrServer.add(sids);
        solrServer.commit();
    }

    public synchronized void deleteIndex(SearchBean bean) throws Exception {
        if(bean == null){
            logger.warn("Get search bean is empty!");
            return;
        }

        String id = bean.getId();

        if(StringUtils.isEmpty(id)){
            logger.warn("get id and id value from bean is empty!");
            return;
        }

        SolrServer server = getSolrServer();
        UpdateResponse ur = server.deleteByQuery("pkId:" + id);
        logger.debug("delete all indexs! UpdateResponse is '{}'! execute for '{}'ms!", ur, ur.getElapsedTime());
        server.commit();
    }

    public synchronized void deleteIndexs(List<SearchBean> beans) throws Exception {
        if(beans == null){
            logger.warn("Get beans is empty!");
            return;
        }

        for(SearchBean bean : beans){
            this.deleteIndex(bean);
        }
    }

    public PaginationSupport doSearch(List<SearchBean> beans, boolean isHighlighter, int start, int num) throws Exception {
        if(beans == null || beans.isEmpty()){
            logger.debug("given search beans is empty!");
            return PaginationUtils.getNullPagination();
        }

        List queryResults = new ArrayList();

        StringBuffer query_ = new StringBuffer();
        for(SearchBean bean : beans){
            //要进行检索的字段
            String[] doSearchFields = bean.getDoSearchFields();
            if(doSearchFields == null || doSearchFields.length == 0)
                continue;

            for(int i = 0; i < doSearchFields.length; i++){
                String f = doSearchFields[i];
                query_.append("(").append(f).append("_message:*").append(bean.getKeyword()).append("*").append(")");

                if(i + 1 != doSearchFields.length)
                    query_.append(" OR ");
            }
        }

        if(StringUtils.isEmpty(query_.toString())){
            logger.warn("query string is null!");
            return PaginationUtils.getNullPagination();
        }

        SolrQuery query = new SolrQuery();
        query.setQuery(query_.toString());
        query.setStart(start == -1 ? 0 : start);
        query.setRows(num == -1 ? 100000000 : num);
        query.setFields("*", "score");

        if(isHighlighter){
            query.setHighlight(true).setHighlightSimplePre(getHtmlPrefix()).setHighlightSimplePost(getHtmlSuffix());
            query.setHighlightSnippets(2);
            query.setHighlightFragsize(1000);
            query.setParam("hl.fl", "*");
        }

        QueryResponse response = getSolrServer().query(query);
        SolrDocumentList sd = response.getResults();

        for(Iterator it = sd.iterator(); it.hasNext(); ){
            SolrDocument doc = (SolrDocument) it.next();
            String indexType = doc.get("indexType").toString();
            SearchBean result = super.getSearchBean(indexType, beans);

            try {
                result.setId(doc.getFieldValue("pkId").toString());
                result.setLink(doc.getFieldValue("link").toString());
                result.setOwerId(doc.getFieldValue("owerId").toString());
                result.setOwerName(doc.getFieldValue("owerName").toString());
                result.setCreateDate(doc.getFieldValue("createDate").toString());
                result.setIndexType(doc.getFieldValue("indexType").toString());

                String keyword = StringUtils.EMPTY;
                if(isHighlighter){
                    String id = (String) doc.getFieldValue("id");
                    List temp = response.getHighlighting().get(id).get("keyword");
                    if(temp != null && !temp.isEmpty()){
                        keyword = temp.get(0).toString();
                    }
                }

                if(StringUtils.isEmpty(keyword))
                    keyword = doc.getFieldValue("keyword").toString();
                result.setKeyword(keyword);

                //要进行检索的字段
                String[] doSearchFields = result.getDoSearchFields();
                if(doSearchFields == null || doSearchFields.length == 0)
                    continue;
                Map<String, String> extendValues = new HashMap<String, String>();
                for(String field : doSearchFields){
                    String value = doc.getFieldValue(field + "_message").toString();
                    if(isHighlighter){
                        String id = (String) doc.getFieldValue("id");
                        List temp = response.getHighlighting().get(id).get(field + "_message");
                        if(temp != null && !temp.isEmpty()){
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

        PaginationSupport paginationSupport = PaginationUtils.makePagination(queryResults, Long.valueOf(sd.getNumFound()).intValue(), num, start);
        return paginationSupport;
    }

    public synchronized void deleteIndexsByIndexType(Class<? extends SearchBean> clazz) throws Exception {
        String indexType = getIndexType(BeanUtils.instantiate(clazz));
        this.deleteIndexsByIndexType(indexType);
    }

    public synchronized void deleteIndexsByIndexType(String indexType) throws Exception {
        SolrServer server = getSolrServer();
        UpdateResponse ur = server.deleteByQuery("indexType:" + indexType);
        logger.debug("delete all indexs! UpdateResponse is '{}'! execute for '{}'ms!", ur, ur.getElapsedTime());
        server.commit();
    }

    public synchronized void deleteAllIndexs() throws Exception {
        SolrServer server = getSolrServer();
        UpdateResponse ur = server.deleteByQuery("*:*");
        logger.debug("delete all indexs! UpdateResponse is '{}'! execute for '{}'ms!", ur, ur.getElapsedTime());
        server.commit();
    }

    public void updateIndex(SearchBean searchBean) throws Exception {
        this.updateIndexs(Collections.singletonList(searchBean));
    }

    /**
     * 更新索引<br/>
     * 在solr中更新索引也就是创建索引(当有相同ID存在的时候,仅仅更新,否则新建)<br/>
     * {@link message.search.engine.SolrSearchEngine#doIndex(java.util.List)}
     *
     * @param searchBeans       需要更新的beans
     * @throws Exception
     */
    public void updateIndexs(List<SearchBean> searchBeans) throws Exception {
        this.doIndex(searchBeans);
    }

    public void setServer(String server) {
        this.server = server;
    }
}

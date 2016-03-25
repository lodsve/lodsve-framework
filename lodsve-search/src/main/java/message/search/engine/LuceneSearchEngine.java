package message.search.engine;

import message.search.bean.SearchBean;
import message.base.utils.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MultiSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 基于lucene实现的索引引擎.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 13-5-5 上午10:38
 */
public class LuceneSearchEngine extends AbstractSearchEngine {
    private static final Logger logger = LoggerFactory.getLogger(LuceneSearchEngine.class);
    /**
     * 索引存放路径
     */
    private String indexPath;
    /**
     * 分词器
     */
    private Analyzer analyzer = new SimpleAnalyzer();

    public synchronized void doIndex(List<SearchBean> searchBeans) throws Exception {
        this.createOrUpdateIndex(searchBeans, true);
    }

    public synchronized void deleteIndex(SearchBean bean) throws Exception {
        if (bean == null) {
            logger.warn("Get search bean is empty!");
            return;
        }

        String id = bean.getId();

        if (StringUtils.isEmpty(id)) {
            logger.warn("get id and id value from bean is empty!");
            return;
        }
        String indexType = getIndexType(bean);
        Directory indexDir = this.getIndexDir(indexType);
        IndexWriter writer = this.getWriter(indexDir);

        writer.deleteDocuments(new Term("pkId", id));
        writer.commit();
        this.destroy(writer);
    }

    public synchronized void deleteIndexs(List<SearchBean> beans) throws Exception {
        if (beans == null) {
            logger.warn("Get beans is empty!");
            return;
        }

        for (SearchBean bean : beans) {
            this.deleteIndex(bean);
        }
    }

    public Page<SearchBean> doSearch(List<SearchBean> beans, boolean isHighlighter, Pageable pageable) throws Exception {
        beans = mergerSearchBean(beans);
        if (beans == null || beans.isEmpty()) {
            logger.debug("given search beans is empty!");
            return new PageImpl<>(Collections.<SearchBean>emptyList(), null, 0);
        }

        IndexSearcher[] searchers = new IndexSearcher[beans.size()];
        for (int i = 0; i < beans.size(); i++) {
            SearchBean bean = beans.get(i);
            String indexType = getIndexType(bean);
            IndexReader reader;
            try {
                reader = IndexReader.open(this.getIndexDir(indexType));
            } catch (Exception e) {
                logger.warn("this folder is not a index directory!");
                continue;
            }
            IndexSearcher searcher = reader != null ? new IndexSearcher(reader) : null;
            searchers[i] = searcher;
        }

        //使用MultiSearcher进行多域搜索
        MultiSearcher searcher = new MultiSearcher(searchers);
        List<String> fieldNames = new ArrayList<>();             //查询的字段名
        List<String> queryValue = new ArrayList<>();             //待查询字段的值
        List<BooleanClause.Occur> flags = new ArrayList<>();
        for (SearchBean bean : beans) {
            //要进行检索的字段
            String[] doSearchFields = bean.getDoSearchFields();
            if (doSearchFields == null || doSearchFields.length == 0)
                return new PageImpl<>(Collections.<SearchBean>emptyList(), null, 0);

            //默认字段
            if (StringUtils.isNotEmpty(bean.getKeyword())) {
                for (String field : doSearchFields) {
                    fieldNames.add(field);
                    queryValue.add(bean.getKeyword());
                    flags.add(BooleanClause.Occur.SHOULD);
                }
            }
        }

        Query query = MultiFieldQueryParser.parse(Version.LUCENE_CURRENT, queryValue.toArray(new String[]{}), fieldNames.toArray(new String[]{}),
                flags.toArray(new BooleanClause.Occur[]{}), analyzer);

        logger.debug("make query string is '{}'!", query.toString());

        ScoreDoc[] scoreDocs = searcher.search(query, 1000000).scoreDocs;

        //查询起始记录位置
        int begin = pageable.getPageNumber() * (pageable.getPageSize());
        //查询终止记录位置
        int end = Math.min((pageable.getPageNumber() + 1) * pageable.getPageSize(), scoreDocs.length);

        //高亮处理
        Highlighter highlighter = null;
        if (isHighlighter) {
            SimpleHTMLFormatter formatter = new SimpleHTMLFormatter(this.getHtmlPrefix(), this.getHtmlSuffix());
            highlighter = new Highlighter(formatter, new QueryScorer(query));
        }

        List<SearchBean> queryResults = new ArrayList<>();
        for (int i = begin; i < end; i++) {
            int docID = scoreDocs[i].doc;
            Document hitDoc = searcher.doc(docID);
            String indexType = hitDoc.get("indexType");
            SearchBean result = super.getSearchBean(indexType, beans);

            if (result == null) continue;

            result.setId(hitDoc.get("pkId"));
            result.setLink(hitDoc.get("link"));
            result.setOwerId(hitDoc.get("owerId"));
            result.setOwerName(hitDoc.get("owerName"));
            result.setCreateDate(hitDoc.get("createDate"));
            result.setIndexType(indexType);

            String keyword = StringUtils.EMPTY;
            if (isHighlighter && highlighter != null)
                keyword = highlighter.getBestFragment(analyzer, "keyword", hitDoc.get("keyword"));

            if (StringUtils.isEmpty(keyword))
                keyword = hitDoc.get("keyword");

            result.setKeyword(keyword);

            //要进行检索的字段
            String[] doSearchFields = result.getDoSearchFields();
            if (doSearchFields == null || doSearchFields.length == 0)
                continue;

            Map<String, String> extendValues = new HashMap<>();
            for (String field : doSearchFields) {
                String value = hitDoc.get(field);
                if (isHighlighter && highlighter != null)
                    value = highlighter.getBestFragment(analyzer, field, hitDoc.get(field));

                if (StringUtils.isEmpty(value))
                    value = hitDoc.get(field);

                extendValues.put(field, value);
            }

            result.setSearchValues(extendValues);

            queryResults.add(result);
        }

        //关闭链接
        searcher.close();
        for (IndexSearcher indexSearcher : searchers) {
            if (indexSearcher != null)
                indexSearcher.close();
        }

        return new PageImpl<>(queryResults, pageable, scoreDocs.length);
    }

    public synchronized void deleteIndexsByIndexType(Class<? extends SearchBean> clazz) throws Exception {
        String indexType = getIndexType(BeanUtils.instantiate(clazz));
        this.deleteIndexsByIndexType(indexType);
    }

    public synchronized void deleteIndexsByIndexType(String indexType) throws Exception {
        //传入readOnly的参数,默认是只读的
        IndexReader reader = IndexReader.open(this.getIndexDir(indexType), false);
        int result = reader.deleteDocuments(new Term("indexType", indexType));
        reader.close();
        logger.debug("the rows of delete index is '{}'! index type is '{}'!", result, indexType);
    }

    public synchronized void deleteAllIndexs() throws Exception {
        File indexFolder = new File(this.indexPath);
        if (indexFolder == null || !indexFolder.isDirectory()) {
            //不存在或者不是文件夹
            logger.debug("indexPath is not a folder! indexPath: '{}'!", indexPath);
            return;
        }

        File[] children = indexFolder.listFiles();
        for (File child : children) {
            if (child == null || !child.isDirectory()) continue;

            String indexType = child.getName();
            logger.debug("Get indexType is '{}'!", indexType);

            this.deleteIndexsByIndexType(indexType);
        }
    }

    public void updateIndex(SearchBean searchBean) throws Exception {
        this.updateIndexs(Collections.singletonList(searchBean));
    }

    public void updateIndexs(List<SearchBean> searchBeans) throws Exception {
        this.createOrUpdateIndex(searchBeans, false);
    }

    /**
     * 创建或者更新索引
     *
     * @param searchBeans 需要创建或者更新的对象
     * @param isCreate    是否是创建索引;true创建索引,false更新索引
     * @throws Exception
     */
    private synchronized void createOrUpdateIndex(List<SearchBean> searchBeans, boolean isCreate) throws Exception {
        if (searchBeans == null || searchBeans.isEmpty()) {
            logger.debug("do no index!");
            return;
        }

        Directory indexDir = null;
        IndexWriter writer = null;
        for (Iterator<SearchBean> it = searchBeans.iterator(); it.hasNext(); ) {
            SearchBean sb = it.next();
            String indexType = getIndexType(sb);
            if (sb == null) {
                logger.debug("give SearchBean is null!");
                return;
            }
            boolean anotherSearchBean = indexDir != null && !indexType.equals(((FSDirectory) indexDir).getFile().getName());
            if (indexDir == null || anotherSearchBean) {
                indexDir = this.getIndexDir(indexType);
            }
            if (writer == null || anotherSearchBean) {
                this.destroy(writer);
                writer = this.getWriter(indexDir);
            }

            Document doc = new Document();

            //初始化一些字段
            sb.initPublicFields();
            String id = sb.getId();

            //主键的索引,不作为搜索字段,并且也不进行分词
            Field idField = new Field("pkId", id, Field.Store.YES, Field.Index.NOT_ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS);
            doc.add(idField);

            logger.debug("create id index for '{}', value is '{}'! index is '{}'!", new Object[]{"pkId", id, idField});

            String owerId = sb.getOwerId();
            if (StringUtils.isEmpty(owerId)) {
                throw new RuntimeException("you must give a owerId");
            }
            Field owerId_ = new Field("owerId", owerId, Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS);
            doc.add(owerId_);

            String owerName = sb.getOwerName();
            if (StringUtils.isEmpty(owerName)) {
                throw new RuntimeException("you must give a owerName");
            }
            Field owerName_ = new Field("owerName", owerName, Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS);
            doc.add(owerName_);

            String link = sb.getLink();
            if (StringUtils.isEmpty(link)) {
                throw new RuntimeException("you must give a link");
            }
            Field link_ = new Field("link", link, Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS);
            doc.add(link_);

            String keyword = sb.getKeyword();
            if (StringUtils.isEmpty(keyword)) {
                throw new RuntimeException("you must give a keyword");
            }
            Field keyword_ = new Field("keyword", keyword, Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS);
            doc.add(keyword_);

            String createDate = sb.getCreateDate();
            if (StringUtils.isEmpty(createDate)) {
                throw new RuntimeException("you must give a createDate");
            }
            Field createDate_ = new Field("createDate", createDate, Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS);
            doc.add(createDate_);

            //索引类型字段
            Field indexType_ = new Field("indexType", indexType, Field.Store.YES, Field.Index.NOT_ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS);
            doc.add(indexType_);

            //进行索引的字段
            String[] doIndexFields = sb.getDoIndexFields();
            Map<String, String> indexFieldValues = sb.getIndexFieldValues();
            if (doIndexFields != null && doIndexFields.length > 0) {
                for (String field : doIndexFields) {
                    String fieldValue = indexFieldValues.get(field);
                    if (StringUtils.isEmpty(fieldValue)) {
                        continue;
                    }

                    Field extInfoField = new Field(field, fieldValue, Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS);
                    doc.add(extInfoField);
                }
            }

            //进行索引的文件字段
            Map<String, File> files = sb.getFileMap();
            if (files != null && !files.isEmpty()) {
                for (Iterator<Map.Entry<String, File>> i = files.entrySet().iterator(); i.hasNext(); ) {
                    Map.Entry<String, File> e = i.next();
                    String column = e.getKey();
                    File file = e.getValue();

                    if (!Arrays.asList(doIndexFields).contains(column)) continue;

                    String content = getFileContent(file);

                    Field extInfoField = new Field(column, content, Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS);
                    doc.add(extInfoField);
                }
            }

            if (isCreate)
                writer.addDocument(doc);
            else
                writer.updateDocument(new Term("pkId", sb.getId()), doc);

            writer.optimize();
        }

        this.destroy(writer);
        logger.debug("create or update index success!");
    }

    private Directory getIndexDir(String suffix) throws Exception {
        File indexDir = new File(indexPath + File.separator + suffix);
        try {
            return FSDirectory.open(indexDir);
        } catch (IOException e) {
            logger.warn("index directory '{}' is not a index directory!", indexDir.getAbsolutePath());
            return null;
        }
    }

    private List<SearchBean> mergerSearchBean(List<SearchBean> beans) {
        List<SearchBean> beans_ = new ArrayList<>();
        if (beans == null || beans.isEmpty()) {
            return beans_;
        }
        for (SearchBean bean : beans) {
            IndexReader reader = null;
            try {
                Directory dir = getIndexDir(bean.getIndexType());
                reader = getReader(dir);
            } catch (Exception e) {

            }

            if (reader != null)
                beans_.add(bean);
        }

        return beans_;
    }

    private IndexWriter getWriter(Directory indexDir) throws IOException {
        return new IndexWriter(indexDir, analyzer, IndexWriter.MaxFieldLength.UNLIMITED);
    }

    private void destroy(IndexWriter writer) throws Exception {
        if (writer != null)
            writer.close();
    }

    private IndexReader getReader(Directory dir) {
        IndexReader reader = null;
        try {
            reader = IndexReader.open(dir);
        } catch (Exception e) {
            logger.warn("this folder '{}' is not a index directory!", dir);
        }

        return reader;
    }

    public void setIndexPath(String indexPath) {
        this.indexPath = indexPath;
    }

    public void setAnalyzer(Analyzer analyzer) {
        this.analyzer = analyzer;
    }

}

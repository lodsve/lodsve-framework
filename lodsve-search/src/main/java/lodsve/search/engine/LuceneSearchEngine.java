/*
 * Copyright © 2009 Sun.Hao(https://www.crazy-coder.cn/)
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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lodsve.search.engine;

import lodsve.core.utils.StringUtils;
import lodsve.search.bean.BaseSearchBean;
import lodsve.search.exception.LuceneException;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * 基于lucene实现的索引引擎.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 13-5-5 上午10:38
 */
public class LuceneSearchEngine extends AbstractSearchEngine {
    private static final Logger logger = LoggerFactory.getLogger(LuceneSearchEngine.class);
    /**
     * 索引存放路径
     */
    private final String indexPath;
    /**
     * 分词器
     */
    private final Analyzer analyzer;

    public LuceneSearchEngine(String indexPath, Analyzer analyzer, String htmlPrefix, String htmlSuffix) {
        super.setHtmlPrefix(htmlPrefix);
        super.setHtmlSuffix(htmlSuffix);

        this.indexPath = indexPath;
        this.analyzer = analyzer == null ? new StandardAnalyzer() : analyzer;
    }

    @Override
    public synchronized void doIndex(List<BaseSearchBean> baseSearchBeans) throws Exception {
        this.createOrUpdateIndex(baseSearchBeans, true);
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
        String indexType = getIndexType(bean);
        Directory indexDir = this.getIndexDir(indexType);
        IndexWriter writer = this.getWriter(indexDir);

        writer.deleteDocuments(new Term("pkId", id));
        writer.commit();
        this.destroy(writer);
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
        beans = mergerBaseSearchBean(beans);
        if (beans == null || beans.isEmpty()) {
            logger.debug("given search beans is empty!");
            return new PageImpl<>(Collections.emptyList(), null, 0);
        }

        IndexReader[] readers = new IndexReader[beans.size()];
        for (int i = 0; i < beans.size(); i++) {
            BaseSearchBean bean = beans.get(i);
            String indexType = getIndexType(bean);

            readers[i] = DirectoryReader.open(getIndexDir(indexType));
        }

        //使用MultiSearcher进行多域搜索
        MultiReader multiReader = new MultiReader(readers);
        //查询的字段名
        List<String> fieldNames = new ArrayList<>();
        //待查询字段的值
        List<String> queryValue = new ArrayList<>();
        List<BooleanClause.Occur> flags = new ArrayList<>();
        for (BaseSearchBean bean : beans) {
            //要进行检索的字段
            String[] doSearchFields = bean.getDoSearchFields();
            if (doSearchFields == null || doSearchFields.length == 0) {
                return new PageImpl<>(Collections.emptyList(), null, 0);
            }

            //默认字段
            if (StringUtils.isNotEmpty(bean.getKeyword())) {
                for (String field : doSearchFields) {
                    fieldNames.add(field);
                    queryValue.add(bean.getKeyword());
                    flags.add(BooleanClause.Occur.SHOULD);
                }
            }
        }

        Query query = MultiFieldQueryParser.parse(
            queryValue.toArray(new String[queryValue.size()]),
            fieldNames.toArray(new String[fieldNames.size()]),
            flags.toArray(new BooleanClause.Occur[flags.size()]), analyzer);

        logger.debug("make query string is '{}'!", query.toString());

        IndexSearcher indexSearcher = new IndexSearcher(multiReader);
        ScoreDoc[] scoreDocs = indexSearcher.search(query, Integer.MAX_VALUE).scoreDocs;

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

        List<BaseSearchBean> queryResults = new ArrayList<>();
        for (int i = begin; i < end; i++) {
            int docID = scoreDocs[i].doc;
            Document hitDoc = indexSearcher.doc(docID);
            String indexType = hitDoc.get("indexType");
            BaseSearchBean result = super.getBaseSearchBean(indexType, beans);

            if (result == null) {
                continue;
            }

            result.setId(hitDoc.get("pkId"));
            result.setLink(hitDoc.get("link"));
            result.setOwerId(hitDoc.get("owerId"));
            result.setOwerName(hitDoc.get("owerName"));
            result.setCreateDate(hitDoc.get("createDate"));
            result.setIndexType(indexType);

            String keyword = StringUtils.EMPTY;
            if (isHighlighter) {
                keyword = highlighter.getBestFragment(analyzer, "keyword", hitDoc.get("keyword"));
            }

            if (StringUtils.isEmpty(keyword)) {
                keyword = hitDoc.get("keyword");
            }

            result.setKeyword(keyword);

            //要进行检索的字段
            String[] doSearchFields = result.getDoSearchFields();
            if (doSearchFields == null || doSearchFields.length == 0) {
                continue;
            }

            Map<String, String> extendValues = new HashMap<>(doSearchFields.length);
            for (String field : doSearchFields) {
                String value = hitDoc.get(field);
                if (isHighlighter) {
                    value = highlighter.getBestFragment(analyzer, field, hitDoc.get(field));
                }

                if (StringUtils.isEmpty(value)) {
                    value = hitDoc.get(field);
                }

                extendValues.put(field, value);
            }

            result.setSearchValues(extendValues);

            queryResults.add(result);
        }

        //关闭链接
        for (IndexReader reader : readers) {
            if (reader != null) {
                reader.close();
            }
        }

        return new PageImpl<>(queryResults, pageable, scoreDocs.length);
    }

    @Override
    public synchronized void deleteIndexsByIndexType(Class<? extends BaseSearchBean> clazz) throws Exception {
        String indexType = getIndexType(BeanUtils.instantiate(clazz));
        this.deleteIndexsByIndexType(indexType);
    }

    @Override
    public synchronized void deleteIndexsByIndexType(String indexType) throws Exception {
        //传入readOnly的参数,默认是只读的
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter indexWriter = new IndexWriter(getIndexDir(indexType), config);

        long result = indexWriter.deleteDocuments(new Term("indexType", indexType));
        indexWriter.close();
        logger.debug("the rows of delete index is '{}'! index type is '{}'!", result, indexType);
    }

    @Override
    public synchronized void deleteAllIndexs() throws Exception {
        File indexFolder = new File(this.indexPath);
        if (!indexFolder.isDirectory()) {
            //不存在或者不是文件夹
            logger.debug("indexPath is not a folder! indexPath: '{}'!", indexPath);
            return;
        }

        File[] children = indexFolder.listFiles();
        if (children == null || children.length == 0) {
            return;
        }

        for (File child : children) {
            if (child == null || !child.isDirectory()) {
                continue;
            }

            String indexType = child.getName();
            logger.debug("Get indexType is '{}'!", indexType);

            this.deleteIndexsByIndexType(indexType);
        }
    }

    @Override
    public void updateIndex(BaseSearchBean searchBean) throws Exception {
        this.updateIndexs(Collections.singletonList(searchBean));
    }

    @Override
    public void updateIndexs(List<BaseSearchBean> searchBean) throws Exception {
        this.createOrUpdateIndex(searchBean, false);
    }

    /**
     * 创建或者更新索引
     *
     * @param searchBean 需要创建或者更新的对象
     * @param isCreate   是否是创建索引;true创建索引,false更新索引
     * @throws Exception
     */
    private synchronized void createOrUpdateIndex(List<BaseSearchBean> searchBean, boolean isCreate) throws Exception {
        if (searchBean == null || searchBean.isEmpty()) {
            logger.debug("do no index!");
            return;
        }

        Directory indexDir = null;
        IndexWriter writer = null;
        for (BaseSearchBean sb : searchBean) {
            String indexType = getIndexType(sb);
            if (sb == null) {
                logger.debug("give BaseSearchBean is null!");
                return;
            }
            boolean anotherBaseSearchBean = indexDir != null && !indexType.equals(((FSDirectory) indexDir).getDirectory().toString());
            if (indexDir == null || anotherBaseSearchBean) {
                indexDir = this.getIndexDir(indexType);
            }
            if (writer == null || anotherBaseSearchBean) {
                this.destroy(writer);
                writer = this.getWriter(indexDir);
            }

            Document doc = new Document();

            //初始化一些字段
            sb.initPublicFields();
            String id = sb.getId();

            //主键的索引,不作为搜索字段,并且也不进行分词
            StringField idField = new StringField("pkId", id, Field.Store.YES);
            doc.add(idField);

            logger.debug("create id index for '{}', value is '{}'! index is '{}'!", "pkId", id, idField);

            String owerId = sb.getOwerId();
            if (StringUtils.isEmpty(owerId)) {
                throw new LuceneException(104009, "you must give a owerId");
            }
            StringField owerIdField = new StringField("owerId", owerId, Field.Store.YES);
            doc.add(owerIdField);

            String owerName = sb.getOwerName();
            if (StringUtils.isEmpty(owerName)) {
                throw new LuceneException(104010, "you must give a owerName");
            }
            StringField owerNameField = new StringField("owerName", owerName, Field.Store.YES);
            doc.add(owerNameField);

            String link = sb.getLink();
            if (StringUtils.isEmpty(link)) {
                throw new LuceneException(104011, "you must give a link");
            }
            StringField linkField = new StringField("link", link, Field.Store.YES);
            doc.add(linkField);

            String keyword = sb.getKeyword();
            if (StringUtils.isEmpty(keyword)) {
                throw new LuceneException(104012, "you must give a keyword");
            }
            StringField keywordField = new StringField("keyword", keyword, Field.Store.YES);
            doc.add(keywordField);

            String createDate = sb.getCreateDate();
            if (StringUtils.isEmpty(createDate)) {
                throw new LuceneException(104013, "you must give a createDate");
            }
            StringField createDateField = new StringField("createDate", createDate, Field.Store.YES);
            doc.add(createDateField);

            //索引类型字段
            StringField indexTypeField = new StringField("indexType", indexType, Field.Store.YES);
            doc.add(indexTypeField);

            //进行索引的字段
            String[] doIndexFields = sb.getDoIndexFields();
            Map<String, String> indexFieldValues = sb.getIndexFieldValues();
            if (doIndexFields != null && doIndexFields.length > 0) {
                for (String field : doIndexFields) {
                    String fieldValue = indexFieldValues.get(field);
                    if (StringUtils.isEmpty(fieldValue)) {
                        continue;
                    }

                    TextField extInfoField = new TextField(field, fieldValue, Field.Store.YES);
                    doc.add(extInfoField);
                }
            }

            if (isCreate) {
                writer.addDocument(doc);
            } else {
                writer.updateDocument(new Term("pkId", sb.getId()), doc);
            }

            writer.commit();
        }

        this.destroy(writer);
        logger.debug("create or update index success!");
    }

    private Directory getIndexDir(String suffix) throws Exception {
        Path path = Paths.get(indexPath, suffix);
        File file = path.toFile();
        if (!file.exists()) {
            if (!file.mkdirs()) {
                throw new LuceneException("create lucene index folder error!");
            }
        }
        return FSDirectory.open(path);
    }

    private List<BaseSearchBean> mergerBaseSearchBean(List<BaseSearchBean> beans) {
        List<BaseSearchBean> beanList = new ArrayList<>();
        if (beans == null || beans.isEmpty()) {
            return beanList;
        }
        for (BaseSearchBean bean : beans) {
            IndexReader reader = null;
            try {
                Directory dir = getIndexDir(bean.getIndexType());
                reader = getReader(dir);
            } catch (Exception ignored) {

            }

            if (reader != null) {
                beanList.add(bean);
            }
        }

        return beanList;
    }

    private IndexWriter getWriter(Directory indexDir) throws IOException {
        IndexWriterConfig writerConfig = new IndexWriterConfig(analyzer);
        writerConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);

        return new IndexWriter(indexDir, writerConfig);
    }

    private void destroy(IndexWriter writer) throws Exception {
        if (writer != null) {
            writer.close();
        }
    }

    private IndexReader getReader(Directory dir) throws IOException {
        return DirectoryReader.open(dir);
    }
}

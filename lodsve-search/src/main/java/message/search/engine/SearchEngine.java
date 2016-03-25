package message.search.engine;

import message.search.bean.SearchBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 索引引擎实现构建索引.删除索引.更新索引.检索等操作.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 13-5-5 上午1:38
 */
public interface SearchEngine {

    /**
     * 创建索引(考虑线程安全)
     *
     * @param searchBeans 对象
     * @throws Exception
     */
    void doIndex(List<SearchBean> searchBeans) throws Exception;

    /**
     * 删除索引
     *
     * @param bean 对象
     * @throws Exception
     */
    void deleteIndex(SearchBean bean) throws Exception;

    /**
     * 删除索引(删除多个)
     *
     * @param beans 对象
     * @throws Exception
     */
    void deleteIndexs(List<SearchBean> beans) throws Exception;

    /**
     * 进行检索
     *
     * @param bean          检索对象(一般只需要放入值keyword,即用来检索的关键字)
     * @param isHighlighter 是否高亮
     * @param pageable      分页信息
     * @return
     * @throws Exception
     */
    Page<SearchBean> doSearch(SearchBean bean, boolean isHighlighter, Pageable pageable) throws Exception;

    /**
     * 进行多个检索对象的检索
     *
     * @param beans         多个检索对象(一般只需要放入值keyword,即用来检索的关键字)
     * @param isHighlighter 是否高亮
     * @param pageable      分页信息
     * @return
     * @throws Exception
     */
    Page<SearchBean> doSearch(List<SearchBean> beans, boolean isHighlighter, Pageable pageable) throws Exception;

    /**
     * 删除某个类型的所有索引(考虑线程安全)
     *
     * @param clazz 索引类型
     * @throws Exception
     */
    void deleteIndexsByIndexType(Class<? extends SearchBean> clazz) throws Exception;

    /**
     * 删除某个类型的所有索引(考虑线程安全)
     *
     * @param indexType 索引类型
     * @throws Exception
     */
    void deleteIndexsByIndexType(String indexType) throws Exception;

    /**
     * 删除所有的索引
     *
     * @throws Exception
     */
    void deleteAllIndexs() throws Exception;

    /**
     * 更新索引
     *
     * @param searchBean 需要更新的bean
     * @throws Exception
     */
    void updateIndex(SearchBean searchBean) throws Exception;

    /**
     * 批量更新索引
     *
     * @param searchBeans 需要更新的beans
     * @throws Exception
     */
    void updateIndexs(List<SearchBean> searchBeans) throws Exception;
}

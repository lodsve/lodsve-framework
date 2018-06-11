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

import lodsve.search.bean.BaseSearchBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 索引引擎实现构建索引.删除索引.更新索引.检索等操作.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 13-5-5 上午1:38
 */
public interface SearchEngine {

    /**
     * 创建索引(考虑线程安全)
     *
     * @param searchBeans 对象
     * @throws Exception
     */
    void doIndex(List<BaseSearchBean> searchBeans) throws Exception;

    /**
     * 删除索引
     *
     * @param bean 对象
     * @throws Exception
     */
    void deleteIndex(BaseSearchBean bean) throws Exception;

    /**
     * 删除索引(删除多个)
     *
     * @param beans 对象
     * @throws Exception
     */
    void deleteIndexs(List<BaseSearchBean> beans) throws Exception;

    /**
     * 进行检索
     *
     * @param bean          检索对象(一般只需要放入值keyword,即用来检索的关键字)
     * @param isHighlighter 是否高亮
     * @param pageable      分页信息
     * @return
     * @throws Exception
     */
    Page<BaseSearchBean> doSearch(BaseSearchBean bean, boolean isHighlighter, Pageable pageable) throws Exception;

    /**
     * 进行多个检索对象的检索
     *
     * @param beans         多个检索对象(一般只需要放入值keyword,即用来检索的关键字)
     * @param isHighlighter 是否高亮
     * @param pageable      分页信息
     * @return
     * @throws Exception
     */
    Page<BaseSearchBean> doSearch(List<BaseSearchBean> beans, boolean isHighlighter, Pageable pageable) throws Exception;

    /**
     * 删除某个类型的所有索引(考虑线程安全)
     *
     * @param clazz 索引类型
     * @throws Exception
     */
    void deleteIndexsByIndexType(Class<? extends BaseSearchBean> clazz) throws Exception;

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
    void updateIndex(BaseSearchBean searchBean) throws Exception;

    /**
     * 批量更新索引
     *
     * @param searchBeans 需要更新的beans
     * @throws Exception
     */
    void updateIndexs(List<BaseSearchBean> searchBeans) throws Exception;
}

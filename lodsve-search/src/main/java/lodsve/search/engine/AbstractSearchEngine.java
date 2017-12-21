package lodsve.search.engine;

import lodsve.core.utils.StringUtils;
import lodsve.search.bean.BaseSearchBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;

/**
 * 搜索引擎的公用方法.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 13-5-8 下午10:53
 */
public abstract class AbstractSearchEngine implements SearchEngine {
    private static final Logger logger = LoggerFactory.getLogger(AbstractSearchEngine.class);

    /**
     * 进行高亮处理时,html片段的前缀
     */
    private String htmlPrefix = "<p>";
    /**
     * 进行高亮处理时,html片段的后缀
     */
    private String htmlSuffix = "</p>";

    public String getHtmlPrefix() {
        return htmlPrefix;
    }

    public void setHtmlPrefix(String htmlPrefix) {
        this.htmlPrefix = htmlPrefix;
    }

    public String getHtmlSuffix() {
        return htmlSuffix;
    }

    public void setHtmlSuffix(String htmlSuffix) {
        this.htmlSuffix = htmlSuffix;
    }

    @Override
    public Page<BaseSearchBean> doSearch(BaseSearchBean bean, boolean isHighlighter, Pageable pageable) throws Exception {
        if (bean == null) {
            logger.debug("given search bean is empty!");
            return new PageImpl<>(Collections.<BaseSearchBean>emptyList(), null, 0);
        }

        return doSearch(Collections.singletonList(bean), isHighlighter, pageable);
    }

    /**
     * 获取index类型
     *
     * @param bean
     * @return
     */
    protected String getIndexType(BaseSearchBean bean) {
        return StringUtils.isNotEmpty(bean.getIndexType()) ? bean.getIndexType() : bean.getClass().getSimpleName();
    }

    /**
     * 根据indexType从BaseSearchBean的集合中获取对应的bean
     *
     * @param indexType
     * @param beans
     * @return
     */
    protected BaseSearchBean getBaseSearchBean(String indexType, List<BaseSearchBean> beans) {
        BaseSearchBean result = null;
        if (StringUtils.isEmpty(indexType) || beans == null || beans.isEmpty()) {
            logger.debug("indexType is null or beans is null!");
            return result;
        }

        for (BaseSearchBean b : beans) {
            if (indexType.equals(b.getIndexType())) {
                result = BeanUtils.instantiate(b.getClass());
                break;
            }
        }

        return result;
    }
}

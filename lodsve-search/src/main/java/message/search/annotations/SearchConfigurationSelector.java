package message.search.annotations;

import message.search.configs.LuceneConfiguration;
import message.search.configs.SolrConfiguration;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择使用的搜索引擎配置.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2016/1/20 12:30
 */
public class SearchConfigurationSelector implements ImportSelector {
    public static final String SEARCH_TYPE_ATTRIBUTE_NAME = "type";

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(EnableSearch.class.getName(), false));
        Assert.notNull(attributes, String.format("@%s is not present on importing class '%s' as expected", EnableSearch.class.getName(), importingClassMetadata.getClassName()));

        List<String> imports = new ArrayList<>();

        SearchType searchType = attributes.getEnum(SEARCH_TYPE_ATTRIBUTE_NAME);
        if (SearchType.SOLR.equals(searchType)) {
            imports.add(SolrConfiguration.class.getName());
        } else if (SearchType.LUCENE.equals(searchType)) {
            imports.add(LuceneConfiguration.class.getName());
        }

        return imports.toArray(new String[imports.size()]);
    }
}

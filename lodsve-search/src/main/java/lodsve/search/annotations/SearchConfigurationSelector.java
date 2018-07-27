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

package lodsve.search.annotations;

import lodsve.search.configs.LuceneConfiguration;
import lodsve.search.configs.SolrConfiguration;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择使用的搜索引擎配置.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2016/1/20 12:30
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

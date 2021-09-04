/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
    private static final String SEARCH_TYPE_ATTRIBUTE_NAME = "type";

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

        return imports.toArray(new String[0]);
    }
}

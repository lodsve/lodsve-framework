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
package lodsve.rdbms.pool;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import com.google.common.collect.Lists;
import lodsve.core.autoproperties.relaxedbind.RelaxedBindFactory;
import lodsve.core.utils.StringUtils;
import lodsve.rdbms.properties.DruidProperties;
import lodsve.rdbms.properties.RdbmsProperties;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;

import java.util.List;

/**
 * druid数据源连接池.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 */
public class DruidDataSourcePool extends BaseDataSourcePool {
    private static final String WALL_FILTER_NAME = "wall";

    DruidDataSourcePool(String dataSourceName, RdbmsProperties rdbmsProperties) {
        super(dataSourceName, rdbmsProperties);
    }

    @Override
    public void setCustomProperties(BeanDefinitionBuilder beanDefinitionBuilder, RdbmsProperties rdbmsProperties) {
        // init method
        beanDefinitionBuilder.setInitMethodName("init");
        // destroy method
        beanDefinitionBuilder.setDestroyMethodName("close");

        DruidProperties druidProperties = new RelaxedBindFactory.Builder<>(DruidProperties.class).build();
        String filters = druidProperties.getFilters();

        WallConfig config = druidProperties.getWallConfig();
        List<Filter> filterList = Lists.newArrayList();
        if (StringUtils.contains(filters, WALL_FILTER_NAME) && null != config) {
            Filter filter = getWallFilter(config);
            filterList.add(filter);
            filters = StringUtils.remove(filters, WALL_FILTER_NAME);
        }

        beanDefinitionBuilder.addPropertyValue("filters", filters);
        beanDefinitionBuilder.addPropertyValue("proxyFilters", filterList);
    }

    private WallFilter getWallFilter(WallConfig config) {
        WallFilter filter = new WallFilter();
        filter.setConfig(config);
        return filter;
    }
}

/*
 * Copyright (C) 2019 Sun.Hao(https://www.crazy-coder.cn/)
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

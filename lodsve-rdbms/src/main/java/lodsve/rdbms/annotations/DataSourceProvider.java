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

package lodsve.rdbms.annotations;

import lodsve.core.configuration.EnableLodsve;
import lodsve.rdbms.configuration.DataSourceBeanDefinitionRegistrar;
import lodsve.rdbms.configuration.DataSourceTransactionManagementConfiguration;
import lodsve.rdbms.configuration.RdbmsConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 数据源提供者.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2017/12/14 下午5:33
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({RdbmsConfiguration.class, DataSourceTransactionManagementConfiguration.class, DataSourceBeanDefinitionRegistrar.class})
@EnableLodsve
public @interface DataSourceProvider {
    /**
     * 选择的数据源
     *
     * @return 数据源名称
     */
    String[] dataSource();
}

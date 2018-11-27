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

package lodsve.mybatis.properties;

import lodsve.core.properties.relaxedbind.annotations.ConfigurationProperties;

/**
 * mybatis 配置.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2018-2-8-0008 16:47
 */
@ConfigurationProperties(prefix = "lodsve.mybatis", locations = "${params.root}/framework/mybatis.properties")
public class MyBatisProperties {
    /**
     * MySQL主键自增长时，缓存的key个数
     */
    private int keyCacheSize = 10;

    public int getKeyCacheSize() {
        return keyCacheSize;
    }

    public void setKeyCacheSize(int keyCacheSize) {
        this.keyCacheSize = keyCacheSize;
    }
}

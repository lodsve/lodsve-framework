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

package lodsve.rdbms.dynamic;

/**
 * 多数据源保存选择的数据源.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2017/12/14 下午6:03
 */
public class DataSourceHolder implements AutoCloseable {
    private static final ThreadLocal<String> DATAS_SOURCE = new ThreadLocal<>();
    private static final DataSourceHolder INSTANCE = new DataSourceHolder();

    public static DataSourceHolder getInstance() {
        return INSTANCE;
    }

    public String get() {
        return DATAS_SOURCE.get();
    }

    public void set(String dataSource) {
        DATAS_SOURCE.set(dataSource);
    }

    @Override
    public void close() {
        DATAS_SOURCE.remove();
    }
}

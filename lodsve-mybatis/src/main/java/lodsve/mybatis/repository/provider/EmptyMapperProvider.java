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

package lodsve.mybatis.repository.provider;

/**
 * 空方法Mapper接口默认MapperTemplate<br/>
 * 如BaseSelectMapper，接口纯继承，不包含任何方法.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 */
public class EmptyMapperProvider extends BaseMapperProvider {

    public EmptyMapperProvider(Class<?> mapperClass) {
        super(mapperClass);
    }

    @Override
    public boolean supportMethod(String msId) {
        return false;
    }
}

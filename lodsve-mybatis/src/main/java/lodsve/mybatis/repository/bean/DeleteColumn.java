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

package lodsve.mybatis.repository.bean;

/**
 * 逻辑删除字段.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 */
public class DeleteColumn extends ColumnBean {
    /**
     * 表示已删除的值，默认为0
     */
    private int delete;
    /**
     * 表示未删除的值，默认为1
     */
    private int nonDelete;

    public DeleteColumn(ColumnBean column) {
        super(column.getTable(), column.getProperty(), column.getColumn(), column.getJavaType());
    }

    public int getDelete() {
        return delete;
    }

    public void setDelete(int delete) {
        this.delete = delete;
    }

    public int getNonDelete() {
        return nonDelete;
    }

    public void setNonDelete(int nonDelete) {
        this.nonDelete = nonDelete;
    }
}

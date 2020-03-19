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

package lodsve.mybatis.repository.bean;

/**
 * 实体字段对应数据库列的信息.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 */
public class ColumnBean {
    /**
     * 对应的表信息
     */
    private EntityTable table;
    /**
     * Java对象中的字段名
     */
    private String property;
    /**
     * 数据库字段名
     */
    private String column;
    /**
     * Java对象中的类型
     */
    private Class<?> javaType;

    ColumnBean(EntityTable table, String property, String column, Class<?> javaType) {
        this.table = table;
        this.property = property;
        this.column = column;
        this.javaType = javaType;
    }

    public ColumnBean(EntityTable table) {
        this.table = table;
    }

    EntityTable getTable() {
        return table;
    }

    void setTable(EntityTable table) {
        this.table = table;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public Class<?> getJavaType() {
        return javaType;
    }

    public void setJavaType(Class<?> javaType) {
        this.javaType = javaType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ColumnBean that = (ColumnBean) o;

        if (column != null ? !column.equals(that.column) : that.column != null) {
            return false;
        }
        if (javaType != null ? !javaType.equals(that.javaType) : that.javaType != null) {
            return false;
        }
        return property != null ? property.equals(that.property) : that.property == null;
    }

    @Override
    public int hashCode() {
        int result = property != null ? property.hashCode() : 0;
        result = 31 * result + (column != null ? column.hashCode() : 0);
        result = 31 * result + (javaType != null ? javaType.hashCode() : 0);
        return result;
    }
}

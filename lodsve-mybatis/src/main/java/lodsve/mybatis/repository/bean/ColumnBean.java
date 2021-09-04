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

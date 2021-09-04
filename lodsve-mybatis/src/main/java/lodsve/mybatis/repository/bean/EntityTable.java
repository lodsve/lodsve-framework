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

import java.util.Set;

/**
 * 实体对应表的配置信息.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 */
public class EntityTable {
    /**
     * 表名
     */
    private String name;
    /**
     * 实体类型
     */
    private Class<?> entityType;
    /**
     * 实体类 => 全部列属性
     */
    private Set<ColumnBean> columns;
    /**
     * 实体类 => 主键信息
     */
    private IdColumn idColumn;
    /**
     * 乐观锁字段
     */
    private VersionColumn versionColumn;
    /**
     * 逻辑删除字段
     */
    private DeleteColumn deleteColumn;
    /**
     * 更新人字段
     */
    private LastModifiedByColumn modifiedByColumn;
    /**
     * 更新时间字段
     */
    private LastModifiedDateColumn modifiedDateColumn;
    /**
     * 禁用时间字段
     */
    private DisabledDateColumn disabledDateColumn;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class<?> getEntityType() {
        return entityType;
    }

    public void setEntityType(Class<?> entityType) {
        this.entityType = entityType;
    }

    public Set<ColumnBean> getColumns() {
        return columns;
    }

    public void setColumns(Set<ColumnBean> columns) {
        this.columns = columns;
    }

    public IdColumn getIdColumn() {
        return idColumn;
    }

    public void setIdColumn(IdColumn idColumn) {
        this.idColumn = idColumn;
    }

    public VersionColumn getVersionColumn() {
        return versionColumn;
    }

    public void setVersionColumn(VersionColumn versionColumn) {
        this.versionColumn = versionColumn;
    }

    public DeleteColumn getDeleteColumn() {
        return deleteColumn;
    }

    public void setDeleteColumn(DeleteColumn deleteColumn) {
        this.deleteColumn = deleteColumn;
    }

    public LastModifiedByColumn getModifiedByColumn() {
        return modifiedByColumn;
    }

    public void setModifiedByColumn(LastModifiedByColumn modifiedByColumn) {
        this.modifiedByColumn = modifiedByColumn;
    }

    public LastModifiedDateColumn getModifiedDateColumn() {
        return modifiedDateColumn;
    }

    public void setModifiedDateColumn(LastModifiedDateColumn modifiedDateColumn) {
        this.modifiedDateColumn = modifiedDateColumn;
    }

    public DisabledDateColumn getDisabledDateColumn() {
        return disabledDateColumn;
    }

    public void setDisabledDateColumn(DisabledDateColumn disabledDateColumn) {
        this.disabledDateColumn = disabledDateColumn;
    }
}

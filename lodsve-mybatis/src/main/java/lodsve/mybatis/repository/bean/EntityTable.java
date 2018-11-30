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

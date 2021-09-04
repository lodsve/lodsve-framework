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

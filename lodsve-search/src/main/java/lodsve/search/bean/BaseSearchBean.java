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
package lodsve.search.bean;

import lodsve.core.utils.DateUtils;
import lodsve.core.utils.ObjectUtils;
import lodsve.core.utils.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 构建索引以及检索使用到的一个抽象类<br/>.
 * 所有需要检索的类均继承这个类,实现{@link BaseSearchBean#getDoSearchFields}和
 * {@link BaseSearchBean#getDoIndexFields}和
 * {@link BaseSearchBean#initPublicFields()}三个方法<br/>
 * <p>
 * eg:<br/>
 * <pre><code>
 *     public class EntitySearchBean extends SearchBean {
 *          public String[] getDoSearchFields() {
 *              return new String[]{"name", "pass", "age"};
 *          }
 *
 *          public String[] getDoIndexFields() {
 *              return new String[]{"name", "pass", "age"};
 *          }
 *
 *          public void initPublicFields() throws Exception {
 *              Object obj = super.getObject();
 *              Entity entity = null;
 *              if(obj instanceof Entity)
 *                  entity = (Entity) obj;
 *
 *              if(entity == null)
 *                  return;
 *
 *              Long pkId = entity.getPkId();
 *              super.setId(pkId.toString());
 *              super.setOwerId("1");
 *              super.setOwerName("孙昊");
 *              super.setLink("/jdbc/addOrUpdateEntity.do?pkId=" + pkId);
 *              super.setContent("一个entity,id为" + pkId);
 *              super.setCreateDate(DateUtils.formatDate(new Date()));
 *          }
 *      }
 * </code></pre>
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 13-5-6 下午4:04
 */
public abstract class BaseSearchBean {
    private static final Logger logger = LoggerFactory.getLogger(BaseSearchBean.class);

    /**************************************以下 共有字段****************************************/
    /**
     * 检索的内容
     */
    private String keyword;
    /**
     * 拥有者ID
     */
    private String owerId;
    /**
     * 拥有者name
     */
    private String owerName;
    /**
     * 检索对象的唯一标识位的值
     */
    private String id;
    /**
     * 检索出对象后进入详情页面的链接
     */
    private String link;
    /**
     * 创建时间
     */
    private String createDate;
    /**
     * index类型
     */
    private String indexType;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getOwerId() {
        return owerId;
    }

    public void setOwerId(String owerId) {
        this.owerId = owerId;
    }

    public String getOwerName() {
        return owerName;
    }

    public void setOwerName(String owerName) {
        this.owerName = owerName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public void setIndexType(String indexType) {
        this.indexType = indexType;
    }
    /**************************************以上 共有字段****************************************/

    /**************************************以下 其他字段****************************************/
    /**
     * 需要检索出来的字段及其值的对应map
     */
    private Map<String, String> searchValues;

    /**
     * 值对象
     */
    private Object object;

    /**
     * 获取检索出来的doIndexFields字段的值
     *
     * @return
     */
    public Map<String, String> getSearchValues() {
        return searchValues;
    }

    /**
     * 设置检索出来的doIndexFields字段的值
     *
     * @param searchValues
     */
    public void setSearchValues(Map<String, String> searchValues) {
        this.searchValues = searchValues;
    }
    /**************************************以上 其他字段****************************************/

    /**************************************以下 抽象方法****************************************/
    /**
     * 返回需要进行检索的字段
     *
     * @return
     */
    public abstract String[] getDoSearchFields();

    /**
     * 进行索引的字段
     *
     * @return
     */
    public abstract String[] getDoIndexFields();

    /**
     * 初始化searchBean中的公共字段(每个对象都必须创建的索引字段)
     *
     * @throws Exception
     */
    public abstract void initPublicFields() throws Exception;

    /**
     * 返回索引类型
     *
     * @return
     */
    public abstract String getIndexType();
    /**************************************以上 抽象方法****************************************/

    /**************************************以下 公共方法****************************************/
    /**
     * 获取需要创建索引字段的键值对map
     *
     * @return
     */
    public Map<String, String> getIndexFieldValues() {
        if (this.object == null) {
            logger.warn("given object is null!");
            return Collections.emptyMap();
        }

        String[] doIndexFields = this.getDoIndexFields();
        if (doIndexFields == null || doIndexFields.length < 1) {
            logger.debug("given no doIndexFields!");
            return Collections.emptyMap();
        }

        Map<String, String> extInfo = new HashMap<>(doIndexFields.length);
        for (String f : doIndexFields) {
            String value = getValue(f);
            if (StringUtils.isNotEmpty(value)) {
                extInfo.put(f, value);
            }
        }

        return extInfo;
    }

    /**
     * 获取一个对象中的某个字段的值,结果转化成string类型
     *
     * @param field 字段名称
     * @return
     */
    private String getValue(String field) {
        if (StringUtils.isEmpty(field)) {
            logger.warn("field is empty!");
            return StringUtils.EMPTY;
        }

        String result;
        Object value = ObjectUtils.getFieldValue(object, field);
        if (value == null) {
            result = StringUtils.EMPTY;
        } else if (value instanceof String) {
            result = (String) value;
        } else if (value instanceof Collections || value instanceof Map) {
            result = ToStringBuilder.reflectionToString(object);
        } else if (value instanceof Date) {
            result = DateUtils.formatDate((Date) value, DateUtils.DEFAULT_PATTERN);
        } else {
            result = value.toString();
        }

        return result;
    }

    /**
     * you must use this method when you create the index, set what object you will to be created its index!
     *
     * @param object the object which you will want to be create index
     */
    public void setObject(Object object) {
        this.object = object;
    }

    /**
     * get what object you want to be created index!
     *
     * @return
     */
    public Object getObject() {
        return this.object;
    }
    /**************************************以上 公共方法****************************************/
}

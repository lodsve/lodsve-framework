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
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 13-5-6 下午4:04
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

        String result = StringUtils.EMPTY;
        try {
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

        } catch (IllegalAccessException e) {
            logger.error("can not find a value for field '{}' in object class '{}'!", field, object.getClass());
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

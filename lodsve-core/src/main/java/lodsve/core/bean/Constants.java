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
package lodsve.core.bean;

/**
 * 资源类型类
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2012-3-14 下午11:56:13
 */
public class Constants {
    /**
     * 标准的日期格式
     */
    public static final String SIMPLE_DATE_FORMAT = "yyyy-MM-dd HH:mm";
    /**
     * 日期格式
     */
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    /**
     * 日期格式
     */
    public static final String DATE_FORMAT_ = "HH:mm yyyy-MM-dd";

    /**
     * request中contextPath
     */
    public static final String DEFAULT_CONTEXT_PATH = "contextPath";

    /**
     * 格式化时间需要的常量
     */
    public static final String DATEFORMAT_CHINESE_FORMAT = "yyyy年MM月dd日 HH时mm分";
    public static final String DATEFORMAT_DATE_FORMAT = "昨天{0}{1}时{2}分";
    public static final String DATEFORMAT_MORNING = "凌晨";
    public static final String DATEFORMAT_NIGHT = "晚上";
    public static final String DATEFORMAT_PM = "下午";
    public static final String DATEFORMAT_AM = "上午";
    public static final String DATEFORMAT_BEFOREH = "小时前";
    public static final String DATEFORMAT_BEFOREM = "分钟前";
    public static final String DATEFORMAT_BEFORES = "秒前";

    /**
     * 所有的标识位
     */
    public static final Long ONE_LONG = 1L;
    public static final Long ZERO_LONG = 0L;
    public static final Integer ONE_INT = 1;
    public static final Integer ZERO_INT = 0;
    /**
     * 没有被删除
     */
    public static final Long DELETE_NO = ZERO_LONG;
    /**
     * 已删除
     */
    public static final Long DELETE_YES = ONE_LONG;
    /**
     * 没有被删除
     */
    public static final Integer DEL_NO = ZERO_INT;
    /**
     * 已删除
     */
    public static final Integer DEL_YES = ONE_INT;

    /**
     * ajax请求后的状态key
     */
    public static final String REQ_STATUS = "status";
    /**
     * ajax请求成功
     */
    public static final Integer REQ_SUCCESS = Integer.valueOf(1);
    public static final String REQ_SUCCESS_STR = "请求成功!";
    /**
     * ajax请求失败
     */
    public static final Integer REQ_FAILURE = Integer.valueOf(0);
    public static final String REQ_FAILURE_STR = "请求失败!";
    /**
     * ajax请求后的信息key
     */
    public static final String REQ_MSG = "msg";
}

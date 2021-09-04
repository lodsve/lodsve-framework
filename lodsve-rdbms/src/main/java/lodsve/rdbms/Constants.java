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
package lodsve.rdbms;

/**
 * 常量.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2017/12/15 下午11:11
 */
public class Constants {
    private Constants() {
    }

    public static final String DATA_SOURCE_ATTRIBUTE_NAME = "dataSource";

    public static final String DATA_SOURCE_BEAN_NAME = "lodsveDataSource";
    public static final String REAL_DATA_SOURCE_BEAN_NAME = "lodsveRealDataSource";

    public static final String DRUID_DATA_SOURCE_CLASS = "com.alibaba.druid.pool.DruidDataSource";

    public static final String FLYWAY_BEAN_NAME = "lodsveFlyway";
}

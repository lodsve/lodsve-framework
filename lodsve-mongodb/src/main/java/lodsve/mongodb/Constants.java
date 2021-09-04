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
package lodsve.mongodb;

/**
 * .
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2018-3-19-0019 13:59
 */
public class Constants {
    public static final String DATA_SOURCE_BEAN_NAME = "lodsveMongoConnection";

    private static final String MAPPING_CONTEXT_BEAN_NAME = "mongoMappingContext";
    public static final String MAPPING_CONVERTER_BEAN_NAME = "mappingConverter";
    public static final String IS_NEW_STRATEGY_FACTORY_BEAN_NAME = "isNewStrategyFactory";
    public static final String DEFAULT_MONGO_TYPE_MAPPER_BEAN_NAME = "defaultMongoTypeMapper";
    public static final String INDEX_HELPER_BEAN_NAME = "indexCreationHelper";
    public static final String CUSTOM_EDITOR_CONFIGURER_BEAN_NAME = "customEditorConfigurer";
    public static final String MONGO_TEMPLATE_BEAN_NAME = "mongoTemplate";
    public static final String MONGO_REPOSITORY_FACTORY_BEAN_NAME = "mongoRepositoryFactory";
    public static final String MONGO_MAPPING_CONTEXT_BEAN_NAME = MAPPING_CONVERTER_BEAN_NAME + "." + MAPPING_CONTEXT_BEAN_NAME;

    public static final String DATA_SOURCE_ATTRIBUTE_NAME = "dataSource";
    public static final String DOMAIN_PACKAGES_ATTRIBUTE_NAME = "domainPackages";
    public static final String BASE_PACKAGES_ATTRIBUTE_NAME = "basePackages";
}

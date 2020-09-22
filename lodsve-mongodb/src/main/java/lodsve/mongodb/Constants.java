/*
 * Copyright © 2009 Sun.Hao(https://www.crazy-coder.cn/)
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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
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

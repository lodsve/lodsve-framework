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
package lodsve.web.springfox.config;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lodsve.core.autoproperties.relaxedbind.annotations.EnableConfigurationProperties;
import lodsve.core.condition.ConditionalOnMissingBean;
import lodsve.core.utils.StringUtils;
import lodsve.web.springfox.properties.SpringFoxProperties;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.schema.AlternateTypeRule;
import springfox.documentation.schema.AlternateTypeRuleConvention;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.ApiSelector;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static com.google.common.collect.Lists.newArrayList;
import static springfox.documentation.schema.AlternateTypeRules.newRule;
import static springfox.documentation.spring.web.plugins.Docket.DEFAULT_GROUP_NAME;

/**
 * 添加swagger部分的包扫描路径.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 16/3/23 下午4:16
 */
@Configuration
@EnableSwagger2
@EnableConfigurationProperties(SpringFoxProperties.class)
@ComponentScan(basePackages = {"lodsve.web.springfox"})
@Profile("springfox")
public class SpringFoxConfiguration implements WebMvcConfigurer, BeanFactoryAware {
    private static final String SPRING_FOX_UI_MAPPING = "/webjars/springfox-swagger-ui/**";
    private static final String SWAGGER_INDEX = "/swagger-ui.html";
    private BeanFactory beanFactory;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(SWAGGER_INDEX).addResourceLocations("classpath:/META-INF/resources/swagger-ui.html");
        registry.addResourceHandler(SPRING_FOX_UI_MAPPING).addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/");
    }

    @Bean
    @ConditionalOnMissingBean
    public AlternateTypeRuleConvention pageableConvention(final TypeResolver resolver) {
        return new AlternateTypeRuleConvention() {
            @Override
            public int getOrder() {
                return Ordered.HIGHEST_PRECEDENCE;
            }

            @Override
            public List<AlternateTypeRule> rules() {
                return newArrayList(newRule(resolver.resolve(Pageable.class), resolver.resolve(Page.class)));
            }
        };
    }

    @Bean
    @ConditionalOnMissingBean
    public List<Docket> createRestApi(SpringFoxProperties springFoxProperties, @Autowired(required = false) List<Parameter> globalParams) {
        ConfigurableBeanFactory configurableBeanFactory = (ConfigurableBeanFactory) beanFactory;
        List<Docket> dockets = new LinkedList<>();

        List<String> groups = springFoxProperties.getGroups();
        if (CollectionUtils.isEmpty(groups)) {
            if (null == groups) {
                groups = Lists.newArrayList();
            }

            groups.add(DEFAULT_GROUP_NAME);
        }

        ApiInfo apiInfo = apiInfo(springFoxProperties);
        groups.stream().filter(StringUtils::isNotBlank).forEach(g -> {
            Predicate<String> pathSelector = ApiSelector.DEFAULT.getPathSelector()::apply;
            pathSelector = pathSelector.and(includePath(g));

            ApiSelectorBuilder apiSelectorBuilder = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo)
                .forCodeGeneration(true)
                .groupName(g)
                .select();

            if (!DEFAULT_GROUP_NAME.equals(g)) {
                apiSelectorBuilder.paths(pathSelector::test);
            }

            Docket docket = apiSelectorBuilder.build();
            if (CollectionUtils.isNotEmpty(globalParams)) {
                docket.globalOperationParameters(globalParams);
            }
            dockets.add(docket);
            configurableBeanFactory.registerSingleton(g, docket);
        });

        return dockets;
    }

    @Bean
    @ConditionalOnMissingBean
    public UiConfiguration uiConfiguration() {
        return UiConfigurationBuilder.builder().build();
    }

    private ApiInfo apiInfo(SpringFoxProperties properties) {
        if (properties == null) {
            return ApiInfo.DEFAULT;
        }

        return new ApiInfoBuilder().title(properties.getTitle()).description(properties.getDescription()).build();
    }

    private Predicate<String> includePath(String groupName) {
        return input -> Pattern.compile(String.format("/%s/.*", groupName)).matcher(input).matches();
    }

    @Override
    public void setBeanFactory(@NonNull BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @ApiModel
    static class Page {
        @ApiModelProperty(value = "当前页码", example = "0")
        private Integer page;
        @ApiModelProperty(value = "每页记录数", example = "10")
        private Integer size;
        @ApiModelProperty("排序,格式{字段名,ASC|DESC},可以多条记录")
        private List<String> sort;

        public Integer getPage() {
            return page;
        }

        public void setPage(Integer page) {
            this.page = page;
        }

        public Integer getSize() {
            return size;
        }

        public void setSize(Integer size) {
            this.size = size;
        }

        public List<String> getSort() {
            return sort;
        }

        public void setSort(List<String> sort) {
            this.sort = sort;
        }
    }
}

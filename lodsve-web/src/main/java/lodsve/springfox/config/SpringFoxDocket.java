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

package lodsve.springfox.config;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import lodsve.core.utils.StringUtils;
import lodsve.mvc.properties.ServerProperties;
import lodsve.springfox.paths.SpringFoxPathProvider;
import lodsve.springfox.properties.SpringFoxProperties;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import springfox.documentation.RequestHandler;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.ApiSelector;
import springfox.documentation.spring.web.plugins.Docket;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.regex.Pattern;

/**
 * 利用插件配置swagger的一些信息.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 16/3/24 上午9:30
 */
public class SpringFoxDocket extends Docket {
    @Autowired
    private SpringFoxPathProvider pathProvider;
    @Autowired
    private SpringFoxProperties springFoxProperties;
    @Autowired
    private ServerProperties serverProperties;

    private static final int SCHEMA_HOST_PATH_LENGTH = 2;
    private String groupName;

    public SpringFoxDocket(String groupName) {
        super(DocumentationType.SWAGGER_2);
        this.groupName = groupName;
    }

    @PostConstruct
    public void init() throws NoSuchFieldException, IllegalAccessException {
        apiInfo(apiInfo(springFoxProperties));
        forCodeGeneration(true);
        groupName(groupName);
        pathProvider(pathProvider);
        host(getHost());

        if (StringUtils.equals(DEFAULT_GROUP_NAME, groupName)) {
            return;
        }

        // 设置apiSelector
        Field apiSelector = this.getClass().getSuperclass().getDeclaredField("apiSelector");
        apiSelector.setAccessible(true);
        Predicate<String> pathSelector = ApiSelector.DEFAULT.getPathSelector();
        pathSelector = Predicates.and(pathSelector, includePath());
        apiSelector.set(this, new ApiSelector(combine(ApiSelector.DEFAULT.getRequestHandlerSelector(), pathSelector), pathSelector));
    }

    private Predicate<RequestHandler> combine(Predicate<RequestHandler> requestHandlerSelector, Predicate<String> pathSelector) {
        return Predicates.and(requestHandlerSelector, transform(pathSelector));
    }

    private Predicate<RequestHandler> transform(final Predicate<String> pathSelector) {
        return new Predicate<RequestHandler>() {
            @Override
            public boolean apply(RequestHandler input) {
                return Iterables.any(input.getRequestMapping().getPatternsCondition().getPatterns(), pathSelector);
            }
        };
    }

    private ApiInfo apiInfo(SpringFoxProperties properties) {
        if (properties == null) {
            return ApiInfo.DEFAULT;
        }

        return new ApiInfo(
                properties.getTitle(),
                properties.getDescription(), StringUtils.EMPTY, StringUtils.EMPTY,
                new Contact(StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY),
                StringUtils.EMPTY, StringUtils.EMPTY
        );
    }

    private String getHost() {
        String serverUrl = serverProperties.getServerUrl();
        if (StringUtils.isBlank(serverUrl)) {
            return "localhost";
        }
        String[] schemaAndHostAndPath = serverUrl.split("://");
        if (ArrayUtils.isEmpty(schemaAndHostAndPath) || SCHEMA_HOST_PATH_LENGTH != ArrayUtils.getLength(schemaAndHostAndPath)) {
            return "localhost";
        }

        String[] hostAndPath = schemaAndHostAndPath[1].split("/");
        if (ArrayUtils.isEmpty(hostAndPath)) {
            return "localhost";
        }

        return hostAndPath[0];
    }

    private Predicate<String> includePath() {
        return new Predicate<String>() {
            @Override
            public boolean apply(String input) {
                return Pattern.compile(String.format("/%s/.*", groupName)).matcher(input).matches();
            }
        };
    }
}

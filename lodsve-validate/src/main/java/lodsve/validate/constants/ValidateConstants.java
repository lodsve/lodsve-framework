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
package lodsve.validate.constants;

import lodsve.core.autoproperties.message.ResourceBundleHolder;
import lodsve.core.utils.PropertyPlaceholderHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * 验证框架的静态常量类.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 12-12-2 上午1:01
 */
public class ValidateConstants {
    private static final Logger logger = LoggerFactory.getLogger(ValidateConstants.class);
    /**
     * 框架中定义的注解
     */
    public static final String[] VALIDATE_ANNOTATIONS = new String[]{
        "Chinese", "Double", "Email", "English", "IdCard", "Integer", "Ip", "Limit", "Mobile",
        "NotNull", "Number", "Password", "Qq", "Regex", "Telephone", "Url", "Zip"
    };

    private static final ResourceBundleHolder RESOURCE_BUNDLE_HOLDER = new ResourceBundleHolder();

    static {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        List<Resource> resources = new ArrayList<>();
        try {
            resources.addAll(Arrays.asList(resolver.getResources("classpath*:/META-INF/validate/*.properties")));
        } catch (IOException e) {
            logger.error("resolver resource:'{classpath*:/META-INF/validate/*.properties}' is error!", e);
            e.printStackTrace();
        }

        for (Resource r : resources) {
            RESOURCE_BUNDLE_HOLDER.loadMessageResource(r);
        }
    }

    public static String getMessage(String key, Object... args) {
        String message = RESOURCE_BUNDLE_HOLDER.getResourceBundle(getLocale()).getString(key);

        return PropertyPlaceholderHelper.replaceNumholder(message, message, args);
    }

    private static Locale getLocale() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (!(requestAttributes instanceof ServletRequestAttributes)) {
            return Locale.getDefault();
        }

        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        return request.getLocale();
    }
}

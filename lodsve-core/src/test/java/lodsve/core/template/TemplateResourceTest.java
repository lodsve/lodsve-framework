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
package lodsve.core.template;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.core.io.Resource;
import org.thymeleaf.templatemode.TemplateMode;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * .
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2017-12-28-0028 13:00
 */
public class TemplateResourceTest {
    private static final Map<String, Object> CONTEXT = new HashMap<>(1);
    private static final String RESULT_EXPECTED = "Hello World!";

    private static final String TEMPLATE_BEETL = "classpath:/template/beetl.txt";
    private static final String TEMPLATE_THYMELEAF = "/template/thymeleaf.txt";

    @BeforeClass
    public static void init() {
        CONTEXT.put("name", "World");
    }

    @Test
    public void testThymeleaf() throws IOException {
        Resource resource = new ThymeleafTemplateResource(TEMPLATE_THYMELEAF, CONTEXT, TemplateMode.TEXT);

        Assert.assertEquals(RESULT_EXPECTED, asString(resource));
    }

    @Test
    public void testBeetl() throws IOException {
        Resource resource = new BeetlTemplateResource(TEMPLATE_BEETL, CONTEXT);

        Assert.assertEquals(RESULT_EXPECTED, asString(resource));
    }

    private String asString(Resource resource) throws IOException {
        return IOUtils.toString(resource.getInputStream());
    }
}

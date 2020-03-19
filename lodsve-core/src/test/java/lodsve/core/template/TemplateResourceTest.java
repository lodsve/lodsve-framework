/*
 * Copyright (C) 2019 Sun.Hao(https://www.crazy-coder.cn/)
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

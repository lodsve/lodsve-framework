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
package lodsve.core.json;

import org.junit.Assert;
import org.junit.Test;

/**
 * .
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2017-12-28-0028 15:26
 */
public class JsonUtilsTest {
    private final Demo demo = new Demo(1L, "demo");
    private final String json = "{\"id\":1,\"name\":\"demo\"}";
    private final String jsonFormat = "{\n" +
        "   \"id\":1,\n" +
        "   \"name\":\"demo\"\n" +
        "}";

    @Test
    public void testJackson() {
        JsonConverter converter = JsonConverterFactory.getConverter(JsonConverterFactory.JsonMode.JACKSON);

        Assert.assertEquals(json, converter.toJson(demo));
        Assert.assertEquals(jsonFormat, converter.toJson(demo, true));
        Assert.assertEquals(demo, converter.toObject(json, Demo.class));
    }

    @Test
    public void testGson() {
        JsonConverter converter = JsonConverterFactory.getConverter(JsonConverterFactory.JsonMode.GSON);

        Assert.assertEquals(json, converter.toJson(demo));
        Assert.assertEquals(demo, converter.toObject(json, Demo.class));
    }

    @Test
    public void testFastJson() {
        JsonConverter converter = JsonConverterFactory.getConverter(JsonConverterFactory.JsonMode.FastJson);

        Assert.assertEquals(json, converter.toJson(demo));
        Assert.assertEquals(demo, converter.toObject(json, Demo.class));
    }

    public static class Demo {
        private Long id;
        private String name;

        public Demo() {
        }

        private Demo(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public int hashCode() {
            return id.hashCode() + name.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Demo && id.equals(((Demo) obj).getId()) && name.equals(((Demo) obj).getName());
        }
    }
}

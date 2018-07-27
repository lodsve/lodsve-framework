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
    private Demo demo = new Demo(1L, "demo");
    private String json = "{\"id\":1,\"name\":\"demo\"}";
    private String jsonFormat = "{\n" +
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

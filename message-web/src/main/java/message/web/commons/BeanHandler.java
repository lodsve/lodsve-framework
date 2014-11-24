package message.web.commons;

/**
 * 每次请求往controller中注入的参数.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 12-6-29 上午12:16
 */
public class BeanHandler {
    private Class clazz;
    private Object value;
    private String name;

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

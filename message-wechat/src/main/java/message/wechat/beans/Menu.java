package message.wechat.beans;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/23 下午4:14
 */
public class Menu {
    private String type;
    private String name;
    private String key;
    private String url;
    @JsonProperty("sub_button")
    private List<Menu> subButtons;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Menu> getSubButtons() {
        return subButtons;
    }

    public void setSubButtons(List<Menu> subButtons) {
        this.subButtons = subButtons;
    }
}

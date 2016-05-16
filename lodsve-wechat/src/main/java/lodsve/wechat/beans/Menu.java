package lodsve.wechat.beans;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

/**
 * 菜单.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/23 下午4:14
 */
@ApiModel(description = "菜单")
public class Menu {
    @ApiModelProperty(value = "类型", required = true)
    private String type;
    @ApiModelProperty(value = "菜单名", required = true)
    private String name;
    @ApiModelProperty(value = "菜单编码", required = true)
    private String key;
    @ApiModelProperty(value = "链接,菜单类型为link时", required = true)
    private String url;
    @ApiModelProperty(value = "子菜单", required = true)
    @JsonProperty("sub_button")
    private List<Menu> subButtons;

    @ApiModelProperty(value = "类型", required = true)
    public String getType() {
        return type;
    }

    @ApiModelProperty(value = "类型", required = true)
    public void setType(String type) {
        this.type = type;
    }

    @ApiModelProperty(value = "菜单名", required = true)
    public String getName() {
        return name;
    }

    @ApiModelProperty(value = "菜单名", required = true)
    public void setName(String name) {
        this.name = name;
    }

    @ApiModelProperty(value = "菜单编码", required = true)
    public String getKey() {
        return key;
    }

    @ApiModelProperty(value = "菜单编码", required = true)
    public void setKey(String key) {
        this.key = key;
    }

    @ApiModelProperty(value = "链接,菜单类型为link时", required = true)
    public String getUrl() {
        return url;
    }

    @ApiModelProperty(value = "链接,菜单类型为link时", required = true)
    public void setUrl(String url) {
        this.url = url;
    }

    @ApiModelProperty(value = "子菜单", required = true)
    public List<Menu> getSubButtons() {
        return subButtons;
    }

    @ApiModelProperty(value = "子菜单", required = true)
    public void setSubButtons(List<Menu> subButtons) {
        this.subButtons = subButtons;
    }
}

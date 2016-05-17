package lodsve.wechat.beans;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 模板消息的数据.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/24 下午4:24
 */
@ApiModel(description = "模板消息的数据")
public class TemplateData {
    @ApiModelProperty(value = "键", required = true)
    private String key;
    @ApiModelProperty(value = "值", required = true)
    private String value;
    @ApiModelProperty(value = "文字颜色", required = true)
    private String color;

    @ApiModelProperty(value = "键", required = true)
    public String getKey() {
        return key;
    }

    @ApiModelProperty(value = "键", required = true)
    public void setKey(String key) {
        this.key = key;
    }

    @ApiModelProperty(value = "值", required = true)
    public String getValue() {
        return value;
    }

    @ApiModelProperty(value = "值", required = true)
    public void setValue(String value) {
        this.value = value;
    }

    @ApiModelProperty(value = "文字颜色", required = true)
    public String getColor() {
        return color;
    }

    @ApiModelProperty(value = "文字颜色", required = true)
    public void setColor(String color) {
        this.color = color;
    }
}

package message.wechat.beans;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * 微信分组.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/3/2 上午11:09
 */
@ApiModel(description = "微信分组")
public class Group {
    /**
     * 分组id，由微信分配
     */
    @ApiModelProperty(value = "id", required = true)
    private int id;
    /**
     * 分组名字，UTF8编码
     */
    @ApiModelProperty(value = "分组名字", required = true)
    private String name;
    /**
     * 分组内用户数量
     */
    @ApiModelProperty(value = "分组内用户数量", required = true)
    private int count;

    @ApiModelProperty(value = "id", required = true)
    public int getId() {
        return id;
    }

    @ApiModelProperty(value = "id", required = true)
    public void setId(int id) {
        this.id = id;
    }

    @ApiModelProperty(value = "分组名字", required = true)
    public String getName() {
        return name;
    }

    @ApiModelProperty(value = "分组名字", required = true)
    public void setName(String name) {
        this.name = name;
    }

    @ApiModelProperty(value = "分组内用户数量", required = true)
    public int getCount() {
        return count;
    }

    @ApiModelProperty(value = "分组内用户数量", required = true)
    public void setCount(int count) {
        this.count = count;
    }
}

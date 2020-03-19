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

package lodsve.wechat.beans;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lodsve.wechat.enums.Lang;
import lodsve.wechat.enums.Sex;

import java.util.Date;

/**
 * 微信用户.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 16/3/2 下午2:13
 */
@ApiModel(description = "模板消息的数据")
public class User {
    /**
     * 用户是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号，拉取不到其余信息。
     */
    @ApiModelProperty(value = "用户是否订阅该公众号标识", required = true)
    private boolean subscribe;
    /**
     * 用户的标识，对当前公众号唯一
     */
    @ApiModelProperty(value = "用户的标识", required = true)
    @JsonProperty("openid")
    private String openId;
    /**
     * 用户的昵称
     */
    @ApiModelProperty(value = "用户的昵称", required = true)
    @JsonProperty("nickname")
    private String nickName;
    /**
     * 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
     */
    @ApiModelProperty(value = "用户的性别", required = true)
    private Sex sex;
    /**
     * 用户所在城市
     */
    @ApiModelProperty(value = "用户所在城市", required = true)
    private String city;
    /**
     * 用户所在国家
     */
    @ApiModelProperty(value = "用户所在国家", required = true)
    private String country;
    /**
     * 用户所在省份
     */
    @ApiModelProperty(value = "用户所在省份", required = true)
    private String province;
    /**
     * 用户的语言，简体中文为zh_CN
     */
    @ApiModelProperty(value = "用户的语言", required = true)
    private Lang language;
    /**
     * 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空。若用户更换头像，原有头像URL将失效。
     */
    @ApiModelProperty(value = "用户头像", required = true)
    @JsonProperty("headimgurl")
    private String headImgUrl;
    /**
     * 用户关注时间，为时间戳。如果用户曾多次关注，则取最后关注时间
     */
    @ApiModelProperty(value = "用户关注时间", required = true)
    @JsonProperty("subscribe_time")
    private Date subscribeTime;
    /**
     * 只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。详见：获取用户个人信息（UnionID机制）
     */
    @ApiModelProperty(value = "unionid", required = true)
    @JsonProperty("unionid")
    private String unionId;
    /**
     * 公众号运营者对粉丝的备注，公众号运营者可在微信公众平台用户管理界面对粉丝添加备注
     */
    @ApiModelProperty(value = "备注", required = true)
    private String remark;
    /**
     * 用户所在的分组ID
     */
    @ApiModelProperty(value = "分组ID", required = true)
    @JsonProperty("groupid")
    private int groupId;

    @ApiModelProperty(value = "用户是否订阅该公众号标识", required = true)
    public boolean isSubscribe() {
        return subscribe;
    }

    @ApiModelProperty(value = "用户是否订阅该公众号标识", required = true)
    public void setSubscribe(boolean subscribe) {
        this.subscribe = subscribe;
    }

    @ApiModelProperty(value = "用户的标识", required = true)
    public String getOpenId() {
        return openId;
    }

    @ApiModelProperty(value = "用户的标识", required = true)
    public void setOpenId(String openId) {
        this.openId = openId;
    }

    @ApiModelProperty(value = "用户的昵称", required = true)
    public String getNickName() {
        return nickName;
    }

    @ApiModelProperty(value = "用户的昵称", required = true)
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @ApiModelProperty(value = "用户的性别", required = true)
    public Sex getSex() {
        return sex;
    }

    @ApiModelProperty(value = "用户的性别", required = true)
    public void setSex(Sex sex) {
        this.sex = sex;
    }

    @ApiModelProperty(value = "用户所在城市", required = true)
    public String getCity() {
        return city;
    }

    @ApiModelProperty(value = "用户所在城市", required = true)
    public void setCity(String city) {
        this.city = city;
    }

    @ApiModelProperty(value = "用户所在国家", required = true)
    public String getCountry() {
        return country;
    }

    @ApiModelProperty(value = "用户所在国家", required = true)
    public void setCountry(String country) {
        this.country = country;
    }

    @ApiModelProperty(value = "用户所在省份", required = true)
    public String getProvince() {
        return province;
    }

    @ApiModelProperty(value = "用户所在省份", required = true)
    public void setProvince(String province) {
        this.province = province;
    }

    @ApiModelProperty(value = "用户的语言", required = true)
    public Lang getLanguage() {
        return language;
    }

    @ApiModelProperty(value = "用户的语言", required = true)
    public void setLanguage(Lang language) {
        this.language = language;
    }

    @ApiModelProperty(value = "用户头像", required = true)
    public String getHeadImgUrl() {
        return headImgUrl;
    }

    @ApiModelProperty(value = "用户头像", required = true)
    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    @ApiModelProperty(value = "用户关注时间", required = true)
    public Date getSubscribeTime() {
        return subscribeTime;
    }

    @ApiModelProperty(value = "用户关注时间", required = true)
    public void setSubscribeTime(Date subscribeTime) {
        this.subscribeTime = subscribeTime;
    }

    @ApiModelProperty(value = "unionid", required = true)
    public String getUnionId() {
        return unionId;
    }

    @ApiModelProperty(value = "unionid", required = true)
    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    @ApiModelProperty(value = "备注", required = true)
    public String getRemark() {
        return remark;
    }

    @ApiModelProperty(value = "备注", required = true)
    public void setRemark(String remark) {
        this.remark = remark;
    }

    @ApiModelProperty(value = "分组ID", required = true)
    public int getGroupId() {
        return groupId;
    }

    @ApiModelProperty(value = "分组ID", required = true)
    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }
}

/*
 * Copyright (C) 2018  Sun.Hao
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

package lodsve.wechat.api.template;

import com.fasterxml.jackson.core.type.TypeReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lodsve.core.utils.StringUtils;
import lodsve.wechat.beans.Industry;
import lodsve.wechat.beans.Template;
import lodsve.wechat.beans.TemplateData;
import lodsve.wechat.core.WeChat;
import lodsve.wechat.core.WeChatRequest;
import lodsve.wechat.core.WeChatUrl;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * 发送模板消息.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/24 下午1:39
 */
@Component
public class WeChatTemplateService {
    /**
     * 设置所属行业,相关代码编号请参考微信公众号开发文档<p/>
     * <a href="http://mp.weixin.qq.com/wiki/17/304c1885ea66dbedf7dc170d84999a9d.html#.E8.AE.BE.E7.BD.AE.E6.89.80.E5.B1.9E.E8.A1.8C.E4.B8.9A">模板消息接口 - 微信公众平台开发者文档</a>
     *
     * @param industryId1 公众号模板消息所属行业编号
     * @param industryId2 公众号模板消息所属行业编号
     */
    public void setIndustry(int industryId1, int industryId2) {
        Map<String, Integer> industry = new HashMap<>(2);
        industry.put("industry_id1", industryId1);
        industry.put("industry_id2", industryId2);

        WeChatRequest.post(String.format(WeChatUrl.API_SET_INDUSTRY, WeChat.accessToken()), industry, new TypeReference<Void>() {
        });
    }

    /**
     * 获取设置的行业信息
     *
     * @return 行业信息
     */
    public Industry getIndustry() {
        return WeChatRequest.get(String.format(WeChatUrl.API_GET_INDUSTRY, WeChat.accessToken()), new TypeReference<Industry>() {
        });
    }

    /**
     * 根据模板短名称获取模板,即在OpenAPI中可以使用
     *
     * @param templateShortId 模板短名称
     * @return 模板id
     */
    public String getTemplateId(String templateShortId) {
        Assert.hasText(templateShortId);

        Map<String, String> params = new HashMap<>(1);
        params.put("template_id_short", templateShortId);

        Map<String, String> result = WeChatRequest.post(String.format(WeChatUrl.API_GET_TEMPLATE, WeChat.accessToken()), params,
                new TypeReference<Map<String, String>>() {
                });

        return MapUtils.isNotEmpty(result) ? result.get("template_id") : StringUtils.EMPTY;
    }

    /**
     * 获取模板列表
     *
     * @return 模板id
     */
    public List<Template> listAllTemplates() {
        Map<String, List<Template>> result = WeChatRequest.get(String.format(WeChatUrl.API_GET_ALL_TEMPLATE, WeChat.accessToken()),
                new TypeReference<Map<String, List<Template>>>() {
                });

        return MapUtils.isNotEmpty(result) ? result.get("template_list") : Collections.<Template>emptyList();
    }

    /**
     * 删除模板列表
     *
     * @return 模板id
     */
    public void deleteTemplate(String templateId) {
        Assert.hasText(templateId);

        Map<String, String> params = new HashMap<>(1);
        params.put("template_id", templateId);

        WeChatRequest.post(String.format(WeChatUrl.API_DELETE_TEMPLATE, WeChat.accessToken()), params,
                new TypeReference<Void>() {
                });
    }

    /**
     * 发送模板消息
     *
     * @param toUser     接受者OpenId
     * @param templateId 使用的模板Id,通过{@link #getTemplateId(String)}获取的
     * @param url        微信中点击消息后进入的页面
     * @param datas      需要的数据
     * @return 消息id
     */
    public String sendTemplateMsg(String toUser, String templateId, String url, List<TemplateData> datas) {
        Assert.hasText(toUser);
        Assert.hasText(templateId);
        Assert.hasText(url);
        Assert.notNull(datas);

        Map<String, Object> params = new HashMap<>(4);
        params.put("touser", toUser);
        params.put("template_id", templateId);
        params.put("url", url);

        Map<String, Map<String, String>> configs = new HashMap<>(datas.size());
        for (TemplateData data : datas) {
            String key = data.getKey();
            String color = data.getColor();
            String value = data.getValue();

            Map<String, String> temp = new HashMap<>(2);
            temp.put("color", color);
            temp.put("value", value);

            configs.put(key, temp);
        }
        params.put("data", configs);

        Map<String, Object> result = WeChatRequest.post(String.format(WeChatUrl.API_SEND_TEMPLATE_MESSAGE, WeChat.accessToken()), params,
                new TypeReference<Map<String, Object>>() {
                });

        return MapUtils.isNotEmpty(result) ? (result.get("msgid") != null ? result.get("msgid").toString() : StringUtils.EMPTY) : StringUtils.EMPTY;
    }
}

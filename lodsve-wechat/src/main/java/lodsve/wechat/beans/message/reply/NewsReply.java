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

package lodsve.wechat.beans.message.reply;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.common.collect.Lists;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lodsve.wechat.beans.message.ReplyType;
import lodsve.wechat.beans.message.reply.items.Article;

/**
 * 回复图文消息.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/23 下午11:41
 */
@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class NewsReply extends Reply {
    public NewsReply() {
        msgType = ReplyType.music;
    }

    @XmlElement(name = "ArticleCount")
    @JSONField(name = "ArticleCount")
    public int articleCount;
    @XmlElement(name = "Articles")
    @JSONField(name = "Articles")
    public List<Article> articles = Lists.newArrayList();
}

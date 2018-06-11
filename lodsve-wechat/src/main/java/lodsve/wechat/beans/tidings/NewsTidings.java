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

package lodsve.wechat.beans.tidings;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lodsve.wechat.beans.tidings.items.Article;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 图文客服消息,带外链.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 16/2/24 下午12:05
 */
@ApiModel(description = "图文客服消息,带外链")
public class NewsTidings extends Tidings {
    public NewsTidings() {
        setTidingsType(TidingsType.news);
    }

    @ApiModelProperty(value = "图文", required = true)
    private Map<String, List<Article>> news;

    @ApiModelProperty(value = "图文", required = true)
    public Map<String, List<Article>> getNews() {
        return news;
    }

    @ApiModelProperty(value = "图文", required = true)
    public void setNews(Map<String, List<Article>> news) {
        if (MapUtils.isEmpty(news)) {
            this.news = news;
        }

        List<Article> articles = new ArrayList<>();
        for (List<Article> a : news.values()) {
            if (CollectionUtils.isNotEmpty(a)) {
                articles.addAll(a);
            }
        }

        this.news = Collections.singletonMap("articles", articles);
    }
}

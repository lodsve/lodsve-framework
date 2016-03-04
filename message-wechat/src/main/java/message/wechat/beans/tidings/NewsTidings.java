package message.wechat.beans.tidings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import message.wechat.beans.tidings.items.Article;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

/**
 * 图文客服消息,带外链.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/24 下午12:05
 */
public class NewsTidings extends Tidings {
    public NewsTidings() {
        setTidingsType(TidingsType.news);
    }

    private Map<String, List<Article>> news;

    public Map<String, List<Article>> getNews() {
        return news;
    }

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

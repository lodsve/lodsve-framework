package message.wechat.beans.message.reply;

import com.google.common.collect.Lists;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import message.wechat.beans.message.ReplyType;
import message.wechat.beans.message.reply.items.Article;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/23 下午11:41
 */
@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class NewsReply extends Reply {
    public NewsReply() {
        setMsgType(ReplyType.news);
    }

    @XmlElement(name = "ArticleCount")
    private int articleCount;
    @XmlElement(name = "Articles")
    private List<Article> articles = Lists.newArrayList();

    public int getArticleCount() {
        return articleCount;
    }

    public void setArticleCount(int articleCount) {
        this.articleCount = articleCount;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }
}

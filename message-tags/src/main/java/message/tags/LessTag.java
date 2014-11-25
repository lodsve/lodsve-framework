package message.tags;

import org.apache.commons.lang.StringUtils;

/**
 * less的自定义标签，为了在引入less时避免重复加上contextPath.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 13-8-4 下午5:51
 */
public class LessTag extends AbstractStaticResourceTag {

    private String charset;

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public void setHref(String href) {
        resourceUrl = href;
    }

    protected String getPrintString(String url) {
        StringBuffer content = new StringBuffer();
        content.append("<link rel=\"stylesheet/less\" type=\"text/css\" ");
        if(StringUtils.isNotEmpty(charset)){
            content.append("charset=\"").append(charset).append("\" ");
        }
        content.append("href=\"").append(url).append("\"/>");

        return content.toString();
    }

    public void release() {
        charset = null;
        resourceUrl = null;
    }
}

package message.tags;

import message.utils.StringUtils;

/**
 * JS的自定义标签，为了在引入JS时避免重复加上contextPath
 * @author sunhao(sunhao.java@gmail.com)
 */
public class JSTag extends AbstractStaticResourceTag {
	private static final long serialVersionUID = -4399738019338181044L;

	private String charset;
	private String language;

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public void setSrc(String src) {
		this.resourceUrl = src;
	}
	
	public void setLanguage(String language) {
		this.language = language;
	}

    protected String getPrintString(String url) {
        if(StringUtils.isEmpty(url)){
            return StringUtils.EMPTY;
        }

        StringBuffer content = new StringBuffer();
        content.append("<script type=\"text/javascript\" ");
        if (StringUtils.isNotEmpty(charset)) {
            content.append("charset=\"").append(charset).append("\" ");
        }
        if (StringUtils.isNotEmpty(language)) {
            content.append("language=\"").append(language).append("\" ");
        }
        content.append(" src=\"").append(url).append("\"></script>");

        return content.toString();
    }

	public void release() {
		charset = null;
		language = null;
        resourceUrl = null;
	}
}

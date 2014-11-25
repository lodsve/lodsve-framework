package message.tags;

import message.utils.StringUtils;

/**
 * css的自定义标签，为了在引入css时避免重复加上contextPath
 * @author sunhao(sunhao.java@gmail.com)
 */
public class CSSTag extends AbstractStaticResourceTag {
	private static final long serialVersionUID = 2967223963177024321L;
	
	private String charset;

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public void setHref(String href) {
		resourceUrl = href;
	}

    protected String getPrintString(String url) {
        StringBuffer content = new StringBuffer();
        content.append("<link rel=\"stylesheet\" type=\"text/css\" ");
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

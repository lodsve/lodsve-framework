package message.tags;

import message.utils.RequestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

/**
 * 为页面request加入ContextPath.
 * 
 * @author Danny(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 2012-12-3 上午12:27:49
 */
public class UrlTag extends TagSupport {
	private static final long serialVersionUID = -739280211072523693L;

	private String value;

	public void setValue(String value) {
		this.value = value;
	}

	public int doEndTag() throws JspException {
		String contextPath = RequestUtils.getContextPath((HttpServletRequest) this.pageContext.getRequest());
		try {
			if (value != null) {
				pageContext.getOut().write(contextPath);

				if (!value.startsWith("/"))
					pageContext.getOut().write("/");

				pageContext.getOut().write(value);

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return SKIP_BODY;
	}
}

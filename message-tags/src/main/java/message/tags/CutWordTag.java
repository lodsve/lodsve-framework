package message.tags;

import message.utils.StringUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

/**
 * 切割姓名字符串的自定义标签
 * @author sunhao(haosun@wisedu.com.cn)
 *
 */
public class CutWordTag extends TagSupport {
	private static final long serialVersionUID = 1L;

	//需要进行切割处理的字符串
	private String cutString;
	//截取前length个字符
	private int length;
	//剩余字符以endString替代
	private String endString = "...";
	
	public String getCutString() {
		return cutString;
	}

	public void setCutString(String cutString) {
		this.cutString = cutString;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public String getEndString() {
		return endString;
	}

	public void setEndString(String endString) {
		this.endString = endString;
	}
	
	
	public int doEndTag() throws JspException {
        String out = "";
        String linkOut = "";
        
    	if(StringUtils.isBlank(cutString)) {
    		out = StringUtils.EMPTY;
    	} else {
    		if(cutString.length() <= length) {
    			out = cutString;
    			linkOut = out;
    		} else {
    			out = cutString.substring(0, length);
    			linkOut = "<div class='showUser' style='display:inline'>" +
						"<span title=\"" + cutString + "\">" +
						out + endString + "</a></div>";
    		}
    	}
        
        print(linkOut);
        return EVAL_PAGE;
    }

    private void print(String linkOut) throws JspTagException {
        try {
            pageContext.getOut().print(linkOut);
        } catch (IOException ioe) {
            throw new JspTagException(ioe.toString(), ioe);
        }
    }

    public void release() {
        cutString = null;
        length = 0;
    }
	
}

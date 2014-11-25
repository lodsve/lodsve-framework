package message.tags;

import message.base.utils.ApplicationHelper;
import message.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.File;
import java.io.IOException;

/**
 * 引用静态资源.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 13-6-30 下午6:24
 */
public abstract class AbstractStaticResourceTag extends TagSupport {
    //资源路径
    protected String resourceUrl;
    //当前时间
    private static final long CURRENT_TIMEMILLIS = System.currentTimeMillis();

    public int doEndTag() throws JspException {
        String url = getVersionUrl(resourceUrl);
        String printString = getPrintString(url);

        if(StringUtils.isNotEmpty(printString))
            this.print(printString);

        return EVAL_PAGE;
    }

    /**
     * 生成带有版本号的路径
     *
     * @return
     */
    private String getVersionUrl(String resourceUrl){
        if(StringUtils.isEmpty(resourceUrl))
            return StringUtils.EMPTY;

        //取得资源路径?前的字符串
        String path = StringUtils.substringBeforeLast(resourceUrl, "?");
        //文件绝对路径
        path = ApplicationHelper.getInstance().getRootPath() + File.separator + path;

        File resource = new File(path);
        long lastModifyTime;
        if(resource.exists()){
            //文件存在
            lastModifyTime = resource.lastModified();
        } else {
            //文件不存在
            lastModifyTime = CURRENT_TIMEMILLIS;
        }

        //生成相对web上下文的路径(url)
        String webPath = ((HttpServletRequest)pageContext.getRequest()).getContextPath() + "/" + resourceUrl;
        //加入版本号
        webPath = webPath + (webPath.indexOf("?") == -1 ? "?" : "&") + "_V=" + lastModifyTime;
        return webPath;
    }

    /**
     * 生成引用的文本
     *
     * @param url
     * @return
     */
    protected abstract String getPrintString(String url);

    private void print(String content) {
        try {
            pageContext.getOut().print(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

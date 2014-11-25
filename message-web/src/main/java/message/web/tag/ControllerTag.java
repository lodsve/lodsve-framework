package message.web.tag;

import message.utils.RequestUtils;
import message.web.core.ControllerForwardCenter;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.tagext.TryCatchFinally;
import java.util.Iterator;
import java.util.Map;

/**
 * 可以在页面上使用tag的形式调用后台controller
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 2012-07-03 下午11:10
 */
public class ControllerTag extends TagSupport implements TryCatchFinally {
	private static final long serialVersionUID = -6210841656653164624L;

    /**
     * 方法名
     */
	private String method;
    /**
     * 模块名
     */
	private String module;
    /**
     * 类型("invoke","content")
     */
    private String type;
    /**
     * 参数(形似:key1=value1&key2=value2)
     */
    private String params;
    

	public void setMethod(String method) {
		this.method = method;
	}

	public void setModule(String module) {
		this.module = module;
	}

    public void setType(String type) {
        this.type = type;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public int doStartTag() throws JspException {
        HttpServletRequest request = (HttpServletRequest) this.pageContext.getRequest();
        HttpServletResponse response = (HttpServletResponse) this.pageContext.getResponse();
        //先处理参数
        //参数在controller中:
        //1.Object obj = in.getAttribute("name");
        //2.String name = (String) obj;
        Map<String, Object> paramsMap = RequestUtils.getParams(this.params);
        for(Iterator it = paramsMap.entrySet().iterator(); it.hasNext(); ){
            Map.Entry entry = (Map.Entry) it.next();
            String key = (String) entry.getKey();
            Object value = entry.getValue();
            request.setAttribute(key, value);
        }
        
        if("invoke".equals(this.type)){
            try {
                doInvoke(request, response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if("content".equals(this.type)){
            try {
                doContent(request, response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
		return 0;
	}

    public void doInvoke(HttpServletRequest req, HttpServletResponse res) throws Exception {
        ModelAndView mv = ControllerForwardCenter.getInstance().invoke(this.module, this.method, req, res);

        //将获取的参数放入页面request中
        if(mv != null){
            Map<String, Object> model = mv.getModel();

            if(model != null && model.size() > 0){
                HttpServletRequest request = (HttpServletRequest) this.pageContext.getRequest();
                Iterator it = model.entrySet().iterator();
                while(it.hasNext()){
                    Map.Entry entry = (Map.Entry) it.next();
                    Object key = entry.getKey();
                    if(key instanceof String){
                        request.setAttribute((String) key, entry.getValue());
                    }
                }
            }
		}
    }

    public void doContent(HttpServletRequest req, HttpServletResponse res) throws Exception {
        //String content = SimpleController.getInstantce().getContent(this.module, this.method, req, res, "");
        //this.pageContext.getOut().print(content);
    }


	public void doCatch(Throwable e) throws Throwable {
		throw e;
	}

	public void doFinally() {
		this.module = null;
        this.method = null;
        this.params = null;
	}

}

package message.web.expand;

import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * 类似ModelAndView,跳转到一个方法.<br/>
 * <p>使用方法:<p>
 * <ul>
 * 	<li>1.<code>return new ModelAndMethod("test", "index");</code>跳转到contextPath/test/index.do</li>
 * 	<li>2.<code>return new ModelAndMethod("test", "index", params);</code>同上,不过给定了一些参数params</li>
 * 	<li>3.<code>return new ModelAndMethod(new ModelAndView("index"));</code>跳转到视图index</li>
 * </ul>
 * 
 * @author Danny Sun(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 2012-10-1 下午03:32:30
 */
public class ModelAndMethod {
	private String module;						//模块名
	private String method;						//方法名
	private Map params;							//参数
	private ModelAndView modelAndView;			//modelAndView

	/************************************************************构造器***************************************************************/
	/**
	 * 含有方法名的构造器,参数没有为null,module默认为这次请求的controller
	 * 
	 * @param method
	 */
	public ModelAndMethod(String method){
		this.method = method;
	}
	
	/**
	 * 含有方法名,参数的构造器,module默认为这次请求的controller
	 * 
	 * @param method
	 */
	public ModelAndMethod(String method, Map params){
		this.method = method;
		this.params = params;
	}
	
	/**
	 * 含有方法名和模块名的构造器,参数没有为null
	 * 
	 * @param module
	 * @param method
	 */
	public ModelAndMethod(String module, String method) {
		this.method = method;
		this.module = module;
	}
	
	/**
	 * 含有方法名和模块名,参数的构造器
	 * 
	 * @param module
	 * @param method
	 * @param params
	 */
	public ModelAndMethod(String module, String method, Map params) {
		this.module = module;
		this.method = method;
		this.params = params;
	}
	
	/**
	 * 含有一个modelAndView的构造器
	 * 
	 * @param modelAndView
	 */
	public ModelAndMethod(ModelAndView modelAndView) {
		this.modelAndView = modelAndView;
	}
	/************************************************************构造器***************************************************************/

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Map getParams() {
		return params;
	}

	public void setParams(Map params) {
		this.params = params;
	}

	public ModelAndView getModelAndView() {
		return modelAndView;
	}

	public void setModelAndView(ModelAndView modelAndView) {
		this.modelAndView = modelAndView;
	}
}

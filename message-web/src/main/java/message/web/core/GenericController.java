package message.web.core;

import message.web.view.UrlMethodNameResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.WebApplicationObjectSupport;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.mvc.multiaction.MethodNameResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * 通用的controller<br/>.
 * 当在配置拦截器的时候,可以把所有的请求指向此controller,<br/>
 * 此controller会将请求发送到相应的controller
 * 
 * @author Danny(sunhao)(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 2012-10-10 下午10:09:22
 */
public class GenericController extends WebApplicationObjectSupport implements Controller {
	private static final Logger logger = LoggerFactory.getLogger(GenericController.class);
	private boolean render;
	private String viewResolverName;
	private LocaleResolver localeResolver;
	private MethodNameResolver methodNameResolver = new UrlMethodNameResolver();
	private ControllerForwardCenter controllerForwardCenter;
	private ViewResolver viewResolver;
	private boolean expires;

    public static final String REQUEST_LOCALE = "REQUEST_LOCALE";

	/**
	 * 构造器
	 */
	public GenericController() {
		this.render = false;
		this.viewResolverName = "viewResolver";
	}

	/**
	 * 统一通用的请求处理
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//国际化处理
		LocaleResolver localResolver = this.localeResolver;
		Locale locale = null;

	    if (localResolver != null) {
	      locale = localResolver.resolveLocale(request);
	    }

	    if (locale == null) {
	      locale = request.getLocale();
	    }
	    request.setAttribute(REQUEST_LOCALE, locale);
	    
	    //返回ModelAndView
	    ModelAndView mav = _handleRequest(request, response);

		if ((mav != null) && (this.expires)) {
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", -3467626328432312320L);
		}

		if ((this.render) && (mav != null)) {
			View view = mav.getView();

			if (view == null) {
				ViewResolver vr = this.viewResolver;

				if (vr != null) {
					view = vr.resolveViewName(mav.getViewName(), locale);
					view.render(mav.getModel(), request, response);

					return null;
				}

				logger.error(this.viewResolverName + " not found.");
			}
		}

	    return mav;
	}
	
	/**
	 * 向这个请求对应的controller发送请求
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ModelAndView _handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
	    request.setCharacterEncoding("UTF-8");
	    String url = this.methodNameResolver.getHandlerMethodName(request);
	    String moduleName = (String)request.getAttribute("URLPARAMID0");
	    return this.controllerForwardCenter.invoke(moduleName, url, request, response);
	  }

	public boolean isRender() {
		return render;
	}

	public void setRender(boolean render) {
		this.render = render;
	}

	public String getViewResolverName() {
		return viewResolverName;
	}

	public void setViewResolverName(String viewResolverName) {
		this.viewResolverName = viewResolverName;
	}

	public LocaleResolver getLocaleResolver() {
		return localeResolver;
	}

	public void setLocaleResolver(LocaleResolver localeResolver) {
		this.localeResolver = localeResolver;
	}

	public MethodNameResolver getMethodNameResolver() {
		return methodNameResolver;
	}

	public void setMethodNameResolver(MethodNameResolver methodNameResolver) {
		this.methodNameResolver = methodNameResolver;
	}

	public ControllerForwardCenter getControllerForwardCenter() {
		return controllerForwardCenter;
	}

	public void setControllerForwardCenter(
			ControllerForwardCenter controllerForwardCenter) {
		this.controllerForwardCenter = controllerForwardCenter;
	}

	public ViewResolver getViewResolver() {
		return viewResolver;
	}

	public void setViewResolver(ViewResolver viewResolver) {
		this.viewResolver = viewResolver;
	}

	public boolean isExpires() {
		return expires;
	}

	public void setExpires(boolean expires) {
		this.expires = expires;
	}

}

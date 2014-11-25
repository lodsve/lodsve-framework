package message.web.view;

import message.utils.StringUtils;
import org.springframework.web.servlet.mvc.multiaction.MethodNameResolver;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import javax.servlet.http.HttpServletRequest;

/**
 * 对请求的URL处理.
 * 
 * @author Danny(sunhao)(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 2012-10-10 下午10:17:32
 */
public class UrlMethodNameResolver implements MethodNameResolver {

	/**
	 * 处理URL<br/>
	 * eg.<br/>
	 * <ul>
	 * <li>1.如果是/test/index1.do,则返回index1,并将test放入request中,key为URLPARAMID<li>
	 * <li>2.如果是/test.do,则返回index,并将test放入request中,key为URLPARAMID</li>
	 * </ul>
	 * 
	 * @param request
	 * @return
	 * @throws org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException
	 */
	public String getHandlerMethodName(HttpServletRequest request) throws NoSuchRequestHandlingMethodException {
		String uri = request.getServletPath();

	    uri = StringUtils.replaceChars(uri, '.', '/');
	    uri = StringUtils.replaceChars(uri, '-', '/');
	    String[] params = StringUtils.split(uri, '/');
	    
		if (params.length > 0) {
			for (int i = 0; i < params.length; ++i)
				request.setAttribute("URLPARAMID" + i, params[i]);
		}
	    
	    if(params.length == 2) {
	    	//第二种情况
	    	return "index";
	    } else if(params.length == 3) {
	    	return params[1];
	    }

	    return "index";
	}

}

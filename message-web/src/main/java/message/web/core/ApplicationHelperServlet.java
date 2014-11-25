package message.web.core;

import message.base.utils.ApplicationHelper;
import org.springframework.web.context.ContextLoader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 重新实现spring的web上下文load类
 * 
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 2012-3-8 下午11:11:21
 */
public class ApplicationHelperServlet extends HttpServlet {
	private static final long serialVersionUID = 1822061516548271940L;
	
	private ContextLoader contextLoader;

	public void init() throws ServletException {
		ApplicationHelper.getInstance().setRootPath(this.getServletContext().getRealPath("/"));
		this.contextLoader = createContextLoader();
		this.contextLoader.initWebApplicationContext(getServletContext());
	}
	
	protected ContextLoader createContextLoader() {
		return new ContextLoader();
	}

	public ContextLoader getContextLoader() {
		return contextLoader;
	}

	public void destroy() {
		ApplicationHelper.getInstance().removeAll();
		if(this.contextLoader != null){
			this.contextLoader.closeWebApplicationContext(getServletContext());
		}
	}

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		getServletContext().log("Attempt to call service method on ContextLoaderServlet as ["
						+ request.getRequestURI() + "] was ignored");
		response.sendError(400);
	}
	
	public String getServletInfo(){
		return getServletInfo();
	}
}
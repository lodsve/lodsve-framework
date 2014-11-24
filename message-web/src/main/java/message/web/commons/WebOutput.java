package message.web.commons;

import com.alibaba.fastjson.JSON;
import message.utils.JsonUtils;
import message.utils.ResultSet;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * 封装HttpServletResponse
 * @author sunhao(sunhao.java@gmail.com)
 */
public class WebOutput {
	private HttpServletResponse response;
	private HttpServletRequest request;
	private static Map _defaultMimeTypes;
	private static Map _defaultCharacter;
	
	static{
		_defaultMimeTypes = new HashMap();
		_defaultMimeTypes.put(".avi", "video/x-msvideo");
		_defaultMimeTypes.put(".bmp", "image/bmp");
		_defaultMimeTypes.put(".class", "application/octet-stream");
		_defaultMimeTypes.put(".css", "text/css");
		_defaultMimeTypes.put(".doc", "application/msword");
		_defaultMimeTypes.put(".exe", "application/octet-stream");
		_defaultMimeTypes.put(".gif", "image/gif");
		_defaultMimeTypes.put(".html", "text/html");
		_defaultMimeTypes.put(".htm", "text/html");
		_defaultMimeTypes.put(".js", "application/x-javascript");
		_defaultMimeTypes.put(".jpeg", "image/jpeg");
		_defaultMimeTypes.put(".jpg", "image/jpeg");
		_defaultMimeTypes.put(".movie", "video/x-sgi-movie");
		_defaultMimeTypes.put(".mp3", "audio/mpeg");
		_defaultMimeTypes.put(".pdf", "application/pdf");
		_defaultMimeTypes.put(".ps", "application/postscript");
		_defaultMimeTypes.put(".ppt", "application/vnd.ms-powerpoint");
		_defaultMimeTypes.put(".png", "image/png");
		_defaultMimeTypes.put(".swf", "application/x-shockwave-flash");
		_defaultMimeTypes.put(".txt", "text/plain");
		_defaultMimeTypes.put(".wav", "audio/x-wav");
		_defaultMimeTypes.put(".xml", "text/xml");
		_defaultMimeTypes.put(".zip", "application/zip");
		
		_defaultCharacter = new HashMap();
		_defaultCharacter.put("gbk", "GBK");
		_defaultCharacter.put("utf8", "UTF-8");
		_defaultCharacter.put("gb2312", "GB2312");
	}

    /**
     * 构造函数
     *
     * @param request
     * @param response
     */
	public WebOutput(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}
	
	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

    /**
     * 设置cookie
     *
     * @param name          name
     * @param value         value
     * @param maxAge        存活时间
     */
	public void setCookie(String name, String value, int maxAge){
		Cookie c = new Cookie(name, value);
		if(maxAge > 0){
			c.setMaxAge(maxAge);
		}
		c.setPath("/");
		this.response.addCookie(c);
	}
	
	public void setContentType(String contentType){
		this.response.setContentType(contentType);
	}
	
	public void setContentType(String contextType, String charset){
		if(charset == null){
			this.response.setContentType(contextType);
		} else {
			this.response.setContentType(contextType + "; charset=" + charset);
		}
	}
	
	public void print(String content) throws IOException{
		PrintWriter out = this.response.getWriter();
		out.write(content);
		out.close();
	}
	
	public void sendRedirect(String url) throws IOException{
		this.response.sendRedirect(url);
	}

    /**
     * 将对象用json的方式给前台,调用对象的toString方法,不推荐使用
     * @param obj
     * @throws Exception
     */
    @Deprecated
	public void toNativeJson(Object obj) throws Exception{
		this.response.setContentType(_defaultMimeTypes.get(".xml") + ";charset=" + _defaultCharacter.get("utf8"));
		this.print(obj.toString());
	}

    /**
     * 使用map的形式生成json数据给前台(flexjson)
     *
     * @param params
     * @throws Exception
     */
	public void toFlexJson(Map<String, ?> params) throws Exception {
		this.response.setContentType(_defaultMimeTypes.get(".html") + ";charset=" + _defaultCharacter.get("utf8"));
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		
		write(JsonUtils.toString(params));
	}

    /**
     * ResultSet传回前台
     *
     * @param rs
     * @throws Exception
     */
    public void toFlexJson(ResultSet rs) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("msg", rs.msg);
        params.put("result", rs.result);
        params.put("status", rs.status);

        toFlexJson(params);
    }

    /**
     * 使用fastjson返回json数据
     *
     * @param params
     */
    public void toFastJson(Map<String, Object> params) throws Exception {
        this.response.setContentType(_defaultMimeTypes.get(".html") + ";charset=" + _defaultCharacter.get("utf8"));
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        write(JSON.toJSONString(params));
    }

    /**
     * ResultSet传回前台
     *
     * @param rs
     * @throws Exception
     */
    public void toFastJson(ResultSet rs) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("msg", rs.msg);
        params.put("result", rs.result);
        params.put("status", rs.status);

        toFastJson(params);
    }
	
	public void write(String content) throws Exception {
		Writer out = response.getWriter();
		out.write(content);
	}
	
	public void flush() throws IOException{
		this.response.flushBuffer();
	}
}

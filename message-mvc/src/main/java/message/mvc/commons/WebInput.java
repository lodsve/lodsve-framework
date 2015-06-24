package message.mvc.commons;

import message.base.Constants;
import message.mvc.editor.CustomDateEditor;
import message.utils.RequestUtils;
import message.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.ServletRequestDataBinder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 封装HttpServletRequest
 * @author sunhao(sunhao.java@gmail.com)
 */
public class WebInput {
    /** Default command name used for binding command objects: "command" */
    public static final String DEFAULT_COMMAND_NAME = "command";

    private static final Logger log = LoggerFactory.getLogger(WebInput.class);
    public static final String TIME_PATTERN = "yyyy-MM-dd HH:mm";

    private static final Integer DEFAULT_INTEGER = Integer.valueOf(-1);
    private static final Long DEFAULT_LONG = Long.valueOf(-1);
    /**
     * session的默认生命周期，是20分钟
     */
    private static final Integer DEFAULT_SESSION_LIFE = 20 * 60 * 1000;
    private HttpServletRequest request;

    public WebInput(HttpServletRequest request){
        this.request = request;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public String getString(String name, String defaultValue) {
        return StringUtils.isEmpty(this.request.getParameter(name)) ? defaultValue : this.request.getParameter(name);
    }

    public String getString(String name){
        return StringUtils.trimToNull(this.request.getParameter(name));
    }

    public int getInt(String name, int defaultValue) {
        int result = 0;
        try {
            result = this.request.getParameter(name) == null ? defaultValue
                    : Integer.parseInt(this.request.getParameter(name));
        } catch (NumberFormatException e) {
            return defaultValue;
        }

        return result;
    }

    public Integer getInt(String name, Integer defaultValue){
        Integer result = defaultValue;
        try {
            result = this.request.getParameter(name) != null ?
                    Integer.valueOf(this.request.getParameter(name)) : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }

        return result;
    }

    public Integer getInt(String name){
        try {
            if(StringUtils.isEmpty(this.request.getParameter(name))){
                return null;
            } else {
                return Integer.parseInt(this.request.getParameter(name));
            }
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public long getLong(String name, long defaultValue){
        long result = defaultValue;
        try {
            result = Long.parseLong(this.request.getParameter(name));
        } catch (NumberFormatException e) {
            return defaultValue;
        }

        return result;
    }

    public Long getLong(String name, Long defaultValue){
        Long result = defaultValue;
        try {
            result = Long.valueOf(this.request.getParameter(name));
        } catch (NumberFormatException e) {
            return defaultValue;
        }

        return result;
    }

    public Long getLong(String name){
        String paramStr = this.request.getParameter(name);
        return StringUtils.isEmpty(paramStr) ? null : Long.valueOf(paramStr);
    }

    public double getDouble(String name, double defaultValue){
        double result = defaultValue;
        try {
            result = Double.parseDouble(this.request.getParameter(name));
        } catch (NumberFormatException e) {
            return 0;
        }

        return result;
    }

    public boolean getBoolean(String name, boolean defaultValue) {
        boolean result = defaultValue;
        String s = StringUtils.trimToNull(this.request.getParameter(name));
        if(s == null){
            return result;
        }
        s = s.toLowerCase();

        if("yes".equals(s) || "true".equals(s) || "1".equals(s) || "t".equals(s)) {
            result = true;
        } else if("no".equals(s) || "false".equals(s) || "0".equals(s) || "f".equals(s)) {
            result = false;
        }

        return result;
    }

    public Date getDate(String name, String pattern, Date defaultValue) {
        DateFormat format = new SimpleDateFormat(pattern);
        Date result = defaultValue;
        try {
            String value = this.request.getParameter(name);
            if(StringUtils.isNotBlank(value)) {
                result = format.parse(value);
            }
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
            return defaultValue;
        }

        return result;
    }

    public Date getDate(String name) {
        DateFormat format = new SimpleDateFormat(TIME_PATTERN);
        String value = this.request.getParameter(name);
        try {
            return StringUtils.isEmpty(value) ? null : format.parse(value);
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public String[] getStrings(String name){
        return this.request.getParameterValues(name);
    }

    public int[] getInts(String name, int defaultValue){
        String[] values = this.getStrings(name);
        if(values == null || values.length < 1){
            return null;
        }
        int[] results = new int[values.length];
        for(int i = 0; i < values.length; i++) {
            results[i] = defaultValue;
            try {
                results[i] = Integer.parseInt(values[i]);
            } catch (NumberFormatException e) {
                log.error(e.getMessage(), e);
            }
        }

        return results;
    }

    public int[] getInts(String name){
        return this.getInts(name, 0);
    }

    public long[] getLongs(String name, long defaultValue){
        String[] values = this.getStrings(name);
        if(values == null || values.length < 1){
            return null;
        }
        long[] results = new long[values.length];
        for(int i = 0; i < values.length; i++) {
            results[i] = defaultValue;
            try {
                results[i] = Long.parseLong(values[i]);
            } catch (NumberFormatException e) {
                log.error(e.getMessage(), e);
            }
        }

        return results;
    }

    public long[] getLongs(String name){
        return this.getLongs(name, 0L);
    }

    public Long[] getLongObjects(String name, Long defaultValue){
        long[] values = this.getLongs(name, defaultValue);
        if(values == null || values.length < 1){
            return null;
        }
        Long[] results = new Long[values.length];
        for(int i = 0; i < values.length; i++){
            results[i] = new Long(values[i]);
        }

        return results;
    }

    public Long[] getLongObjects(String name){
        return this.getLongObjects(name, 0L);
    }

    public String getCookieValue(String name){
        Cookie[] cs = this.request.getCookies();

        if(cs == null || cs.length < 1){
            return null;
        }

        for(int i = 0; i < cs.length; i++){
            Cookie c = cs[i];
            String key = c.getName();
            String value = c.getValue();
            if(name.equals(key)){
                return value;
            }
        }

        return null;
    }

    public HttpSession getSession(){
        return this.request.getSession();
    }

    public void setMaxInactiveInterval(HttpSession session, Integer maxTime){
        session.setMaxInactiveInterval(maxTime == null ? DEFAULT_SESSION_LIFE : maxTime);
    }

    public String getClientIP(){
        return this.request.getRemoteAddr();
    }

    public void setAttribute(String name, Object value){
        request.setAttribute(name, value);
    }

    public Object getAttribute(String name){
        return this.request.getAttribute(name);
    }

    /**
     * 从request中获取一个bean
     *
     * @param beanClass     bean的class
     * @param <T>
     * @return
     */
    public <T> T getBean(Class<T> beanClass){
        Object command = BeanUtils.instantiate(beanClass);
        try {
            bind(request, command);
        } catch (Exception e) {
            log.debug("can not find ant bean use class '{}' from request!", beanClass);
        }

        return (T) command;
    }

    /**
     * Bind request parameters onto the given command bean
     * @param request request from which parameters will be bound
     * @param command command object, that must be a JavaBean
     * @throws Exception in case of invalid state or arguments
     */
    private void bind(HttpServletRequest request, Object command) throws Exception {
        log.debug("Binding request parameters onto MultiActionController command");
        ServletRequestDataBinder binder = createBinder(request, command);

        binder.bind(request);
    }

    /**
     * Create a new binder instance for the given command and request.
     * <p>Called by <code>bind</code>. Can be overridden to plug in custom
     * ServletRequestDataBinder subclasses.
     * <p>The default implementation creates a standard ServletRequestDataBinder,
     * and invokes <code>initBinder</code>. Note that <code>initBinder</code>
     * will not be invoked if you override this method!
     * @param request current HTTP request
     * @param command the command to bind onto
     * @return the new binder instance
     * @throws Exception in case of invalid state or arguments
     * @see #bind
     */
    private ServletRequestDataBinder createBinder(HttpServletRequest request, Object command) throws Exception {
        List<String> params = RequestUtils.getRequestParam(request);
        ServletRequestDataBinder binder = new ServletRequestDataBinder(command, DEFAULT_COMMAND_NAME);

        DateFormat dateFormat1 = new SimpleDateFormat(getDateFormatPattern(params));
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat1, true));

        return binder;
    }

    /**
     * get date format pattern for what parameter in request
     * @param urlParams
     * @return
     */
    private String getDateFormatPattern(List<String> urlParams){
        if(urlParams.isEmpty()){
            log.debug("this url paramter is null!");
            return Constants.SIMPLE_DATE_FORMAT;
        }
        //yyyy-MM-dd hh:mm
        Pattern p1 = Pattern.compile("(\\d){2,4}[-](\\d){1,2}[-](\\d){1,2} (\\d){1,2}[:](\\d){1,2}");
        //yyyy-MM-dd
        Pattern p2 = Pattern.compile("(\\d){2,4}[-](\\d){1,2}[-](\\d){1,2}");
        //hh:mm yyyy-MM-dd
        Pattern p3 = Pattern.compile("(\\d){1,2}[:](\\d){1,2} (\\d){2,4}[-](\\d){1,2}[-](\\d){1,2}");
        for(String up : urlParams){
            if(p1.matcher(up).matches()){
                return Constants.SIMPLE_DATE_FORMAT;
            }
            if(p2.matcher(up).matches()){
                return Constants.DATE_FORMAT;
            }
            if(p3.matcher(up).matches()){
                return Constants.DATE_FORMAT_;
            }
        }

        return Constants.SIMPLE_DATE_FORMAT;
    }
}

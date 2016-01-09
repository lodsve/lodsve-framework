package message.exception.core;

import message.base.Constants;
import message.base.exception.ApplicationRuntimeException;
import message.config.SystemConfig;
import message.config.loader.i18n.ResourceBundleHolder;
import message.utils.JsonUtils;
import message.utils.PropertyPlaceholderHelper;
import message.utils.RequestUtils;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;
import java.util.Locale;

/**
 * 异常统一处理类.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 14-9-11 下午9:04
 */
public class ApplicationExceptionResolver extends SimpleMappingExceptionResolver {
    private static final String ERROR_MESSAGE_KEY = "exceptionMsg";

    private static final String AJAX_TYPE_JSON = "json";
    private static final String AJAX_TYPE_HTML = "html";
    private static final String AJAX_TYPE_JS = "js";
    private static final String AJAX_ERROR_HTML = "An error occured when dealing with the ajax request,nested exception is : :errorMessage";
    private static final String AJAX_ERROR_JS = "javascript:alert(\":errorMessage \")";

    /**
     * 加载了所有的资源文件信息.
     */
    private ResourceBundleHolder resourceBundleHolder = new ResourceBundleHolder();

    public void init() throws Exception {
        String folderPath = "error";
        Resource resource = SystemConfig.getConfigFile(folderPath);

        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = new Resource[0];
        try {
            resources = resolver.getResources("file:" + resource.getFile().getAbsolutePath() + "/*.properties");
        } catch (IOException e) {
            logger.error("resolver resource:'{" + resource + "}' is error!", e);
            e.printStackTrace();
        }

        for (Resource r : resources) {
            String filePath = r.getFile().getAbsolutePath();
            if (message.utils.StringUtils.indexOf(filePath, "_") == -1) {
                this.resourceBundleHolder.loadMessageResource(filePath, 1);
            }
        }
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        if (RequestUtils.isAjaxRequest(request)) {
            Enumeration ajaxParams = request.getParameterNames();
            String ajaxParam = null;

            while (ajaxParams != null && ajaxParams.hasMoreElements()) {
                Object obj = ajaxParams.nextElement();
                if (obj != null && obj instanceof String && StringUtils.equalsIgnoreCase("ajaxtype", (String) obj)) {
                    ajaxParam = (String) obj;
                    break;
                }
            }

            String ajaxType = ajaxParam == null ? AJAX_TYPE_JSON : request.getParameter(ajaxParam);
            ajaxType = ajaxType == null ? AJAX_TYPE_JSON : ajaxType;

            if (ajaxType != null) {
                return resolveAjaxException(ajaxType, response, ex, request);
            }
        }

        return super.resolveException(request, response, handler, ex);
    }

    @Override
    protected ModelAndView getModelAndView(String viewName, Exception ex, HttpServletRequest request) {
        ModelAndView mv = super.getModelAndView(viewName, ex, request);

        String errorMessage = this.getExceptionMessage(ex, request);

        mv.addObject(ERROR_MESSAGE_KEY, errorMessage);

        return mv;
    }

    private ModelAndView resolveAjaxException(String ajaxType, HttpServletResponse response, Exception ex, HttpServletRequest request) {
        String exceptionMessage = getExceptionMessage(ex, request);

        if (StringUtils.equalsIgnoreCase(AJAX_TYPE_JSON, ajaxType)) {
            JSONObject params = new JSONObject();
            params.put(Constants.REQ_STATUS, Constants.REQ_FAILURE);
            params.put("msg", exceptionMessage);

            printHTML(response, JsonUtils.toString(params));
        } else if (StringUtils.equalsIgnoreCase(AJAX_TYPE_HTML, ajaxType)) {
            printHTML(response, AJAX_ERROR_HTML);
        } else if (StringUtils.equals(AJAX_TYPE_JS, ajaxType)) {
            printHTML(response, AJAX_ERROR_JS);
        }
        return null;
    }

    private String getExceptionMessage(Exception ex, HttpServletRequest request) {
        String exceptionMessage;
        if (ex instanceof ApplicationRuntimeException) {
            //系统自定义的异常
            exceptionMessage = getApplicationExceptionMessage(ex, request);
        } else {
            //未知异常
            exceptionMessage = getUnknownExceptionMessage(ex);
        }

        return exceptionMessage;
    }

    /**
     * 获取异常的异常信息
     *
     * @param ex 异常
     * @return
     */
    private String getUnknownExceptionMessage(Exception ex) {
        String errorMsg = ex.getMessage();

        if (StringUtils.isEmpty(errorMsg) && (ex instanceof InvocationTargetException)) {
            Throwable t = ((InvocationTargetException) ex).getTargetException();
            if (t != null) {
                errorMsg = t.getMessage();
            }
        }
        return StringUtils.trimToEmpty(errorMsg);
    }

    /**
     * 获取系统定义异常的信息
     *
     * @param ex
     * @param request
     * @return
     */
    private String getApplicationExceptionMessage(Exception ex, HttpServletRequest request) {
        if (!(ex instanceof ApplicationRuntimeException)) {
            return "未知异常";
        }

        ApplicationRuntimeException are = (ApplicationRuntimeException) ex;
        int errorCode = are.getErrorCode();
        Locale locale = request.getLocale();

        String messageByCode = this.resourceBundleHolder.getResourceBundle(locale).getString(errorCode + "");

        String message = formatErrorMessage(are.getMessage(), are.getArgs());

        Throwable throwable = are.getException();
        if (throwable != null) {
            throwable.printStackTrace();
        }

        return messageByCode + "[" + message + "]";
    }

    private String formatErrorMessage(String message, String[] args) {
        if (args == null || args.length == 0) {
            return message;
        }

        return PropertyPlaceholderHelper.replacePlaceholder(message, message, args);
    }

    private void printHTML(HttpServletResponse response, String html) {
        PrintWriter printWriter;
        try {
            response.setContentType("application/x-json");
            response.setCharacterEncoding("utf-8");
            printWriter = response.getWriter();
            printWriter.print(html);
            printWriter.close();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
    }
}

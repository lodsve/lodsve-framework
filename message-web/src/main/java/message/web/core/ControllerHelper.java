package message.web.core;

import message.base.Constants;
import message.web.ajax.AjaxControllerInternal;
import message.web.commons.WebInput;
import message.web.commons.WebOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * ajax请求的帮助类.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 12-9-6 下午11:27
 */
public class ControllerHelper {
    private static final Logger logger = LoggerFactory.getLogger(ControllerHelper.class);

    /**
     * ajax请求处理
     * 
     * @param in
     * @param out
     * @param extParam                      扩展参数
     * @param ajaxControllerInternal        内部参数
     * @return
     * @throws Exception
     */
    public static ModelAndView ajaxController(WebInput in, WebOutput out, Object extParam, AjaxControllerInternal ajaxControllerInternal) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        try {
            Integer status = ajaxControllerInternal.doInternal(in, out, params, extParam);
            params.put(Constants.REQ_STATUS, status);
        } catch (Exception e) {
            params.put(Constants.REQ_STATUS, Constants.REQ_FAILURE);
            params.put(Constants.REQ_MSG, e.getLocalizedMessage());
            logger.error(e.getLocalizedMessage());
        }

        out.toFlexJson(params);
        return null;
    }

}

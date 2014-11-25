package message.web.ajax;

import message.web.commons.WebInput;
import message.web.commons.WebOutput;

import java.util.Map;

/**
 * 处理ajax请求的内部接口.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 12-9-6 下午11:29
 */
public interface AjaxControllerInternal<T> {

    /**
     * ajax请求中,处理请求内部的方法
     * 
     * @param in
     * @param out
     * @param params
     * @param extParam      扩展参数
     * @return
     * @throws Exception
     */
    Integer doInternal(WebInput in, WebOutput out, Map<String, Object> params, T extParam) throws Exception;

}

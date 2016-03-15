package message.base.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HttpClient的工具类.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0 13-12-11 下午6:50
 */
public class HttpClientUtils {
    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);

    /**
     * Default charset for each request.
     */
    private static final String DEFAULT_CHARSET = "UTF-8";
    /**
     * Default timeout for each request.
     */
    private static final int DEFAULT_TIMEOUT = 5000;

    /**
     * 构造器私有化
     */
    private HttpClientUtils() {
    }

    /**
     * 发送一个post请求
     *
     * @param url    地址
     * @param params 参数
     * @return 返回结果
     * @throws java.io.IOException
     */
    public static String post(String url, Map<String, String> params) throws IOException {
        PostMethod method = new PostMethod(url);

        method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, DEFAULT_CHARSET);
        method.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, DEFAULT_TIMEOUT);

        if (MapUtils.isNotEmpty(params)) {
            List<NameValuePair> pairs = new ArrayList<>(params.size());
            for (Map.Entry<String, String> entry : params.entrySet()) {
                NameValuePair pair = new NameValuePair();
                pair.setName(entry.getKey());
                pair.setValue(entry.getValue());

                pairs.add(pair);
            }

            method.addParameters(pairs.toArray(new NameValuePair[pairs.size()]));
        }

        return executeMethod(method);
    }

    /**
     * 发送一个get请求
     *
     * @param url 地址
     * @return
     * @throws java.io.IOException
     */
    public static String get(String url) throws IOException {
        GetMethod method = new GetMethod(url);
        HttpMethodParams hmp = method.getParams();

        hmp.setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, DEFAULT_CHARSET);
        hmp.setParameter(HttpMethodParams.SO_TIMEOUT, DEFAULT_TIMEOUT);

        return executeMethod(method);
    }

    /**
     * 执行post或者get方法
     *
     * @param method post或者get方法
     * @return
     * @throws java.io.IOException
     */
    private static String executeMethod(HttpMethodBase method) throws IOException {
        HttpClient httpClient = new HttpClient();

        try {
            int statusCode = httpClient.executeMethod(method);

            if (statusCode != HttpStatus.SC_OK) {
                throw new IOException("Method failed: " + method.getStatusLine());
            }

            // get response stream.
            InputStream stream = method.getResponseBodyAsStream();

            // charset
            String charset = method.getRequestCharSet();
            String response;
            try {
                // try to turn stream to bytes.
                byte[] responseBytes = IOUtils.toByteArray(stream);
                // decode with the reponse charset.
                if (StringUtils.isNotBlank(charset)) {
                    try {
                        response = new String(responseBytes, charset);
                    } catch (UnsupportedEncodingException e) {
                        logger.error(e.getMessage(), e);
                        response = new String(responseBytes);
                    }
                } else {
                    // decode with default charset.
                    response = new String(responseBytes);
                }

                return response;
            } finally {
                // close stream.
                IOUtils.closeQuietly(stream);
            }
        } finally {
            // release connection.
            method.releaseConnection();
        }
    }
}

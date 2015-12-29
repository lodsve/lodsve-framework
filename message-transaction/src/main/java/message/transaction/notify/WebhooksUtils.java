package message.transaction.notify;

import com.pingplusplus.model.Event;
import com.pingplusplus.model.Webhooks;
import message.transaction.InvalidWeebhooksException;
import message.transaction.utils.PingConfig;
import message.utils.StringUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;

/**
 * ping++的回调校验.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/10/26 下午5:06
 */
@Component
public final class WebhooksUtils implements ResourceLoaderAware {
    private static final String SIGNATURE_IN_HEADER = "x-pingplusplus-signature";
    private static final String KEY_ALGORITHM = "RSA";
    private static final String SIGN_ALGORITHM = "SHA256withRSA";
    private static final String REGEX_PUB_KEY = "(-+BEGIN PUBLIC KEY-+\\r?\\n|-+END PUBLIC KEY-+\\r?\\n?)";

    private static PingConfig pingConfig;
    private static ResourceLoader resourceLoader;

    @Autowired
    public WebhooksUtils(PingConfig pingConfig) {
        WebhooksUtils.pingConfig = pingConfig;
    }

    /**
     * 将回调的request转成Event
     *
     * @param request
     * @return
     * @throws Exception
     */
    public static Event parsePingEvent(HttpServletRequest request) throws Exception {
        // 1. 校验
        String signature = request.getHeader(SIGNATURE_IN_HEADER);
        String eventBody = evalPingRequest(request);

        validateHooks(signature, eventBody);

        // 2. 解析
        return Webhooks.eventParse(eventBody);
    }

    /**
     * 对请求进行校验
     *
     * @param signature header中的签名
     * @param eventBody event
     * @throws Exception
     */
    public static void validateHooks(String signature, String eventBody) throws Exception {
        // 1. 从 header 取出签名字段
        // 2. base64 解码
        byte[] signatureBytes = Base64.decodeBase64(signature);
        // 3. 获取 Webhooks 请求的原始数据
        // 4. 将获取到的 Webhooks 通知、Ping++ 管理平台提供的 RSA 公钥、和 base64 解码后的签名三者一同放入 RSA 的签名函数中进行非对称的签名运算，来判断签名是否验证通过
        boolean result = verifySignature(eventBody.getBytes(), signatureBytes, getPubKey());
        if (!result) {
            throw new InvalidWeebhooksException(-1, String.format("invalid webhooks!request body is %s!", eventBody));
        }
    }

    /**
     * 从event的object字段取值
     *
     * @param event
     * @param key
     * @param <T>
     * @return
     */
    public static <T> T findDataFromEventData(Event event, String key) {
        if (event == null || StringUtils.isBlank(key)) {
            return null;
        }

        Map<String, Object> data = (Map<String, Object>) event.getData().get("object");
        if (MapUtils.isEmpty(data)) {
            return null;
        }

        return (T) data.get(key);
    }

    /**
     * 将回调的request转成Event的json对象
     *
     * @param request
     * @return
     * @throws Exception
     */
    private static String evalPingRequest(HttpServletRequest request) throws Exception {
        Assert.notNull(request);

        request.setCharacterEncoding("UTF8");
        // 获得 http body 内容
        BufferedReader reader = request.getReader();
        StringBuilder buffer = new StringBuilder();
        String string;
        while ((string = reader.readLine()) != null) {
            buffer.append(string);
        }

        return buffer.toString();
    }

    /**
     * 获得公钥
     *
     * @return
     * @throws Exception
     */
    private static PublicKey getPubKey() throws Exception {
        String pubKeyPath = pingConfig.getPubKey();
        Resource resource = resourceLoader.getResource(pubKeyPath);
        String pubKey = IOUtils.toString(resource.getInputStream());

        pubKey = pubKey.replaceAll(REGEX_PUB_KEY, StringUtils.EMPTY);

        byte[] keyBytes = Base64.decodeBase64(pubKey);

        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicKey = keyFactory.generatePublic(spec);
        return publicKey;
    }

    /**
     * 验证签名
     *
     * @param data
     * @param signatureBytes
     * @param publicKey
     * @return
     * @throws Exception
     */
    private static boolean verifySignature(byte[] data, byte[] signatureBytes, PublicKey publicKey) throws Exception {
        Signature signature = Signature.getInstance(SIGN_ALGORITHM);
        signature.initVerify(publicKey);
        signature.update(data);
        return signature.verify(signatureBytes);
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        WebhooksUtils.resourceLoader = resourceLoader;
    }
}

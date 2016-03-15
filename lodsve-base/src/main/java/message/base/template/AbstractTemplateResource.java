package message.base.template;

import org.springframework.core.io.AbstractResource;

import java.io.*;
import java.util.Map;

/**
 * 利用模板引擎生成Spring Resource.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 14-8-30 上午1:04
 */
public abstract class AbstractTemplateResource extends AbstractResource {
    /**
     * 模板路径
     */
    protected String template;
    /**
     * 模板需要的参数
     */
    protected Map<String, ?> context;
    /**
     * 描述，即来源
     */
    protected String description;

    protected AbstractTemplateResource(String template, Map<String, String> context) {
        this.template = template;
        this.context = context;
    }

    protected AbstractTemplateResource(String template, Map<String, ?> context, String description) {
        this.template = template;
        this.context = context;
        this.description = description == null ? "from template" : description;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        byte[] bytes = null;
        try {
            bytes = renderTemplate().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
        }

        return new ByteArrayInputStream(bytes);
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    public abstract String renderTemplate();
}

package lodsve.core.template;

import org.springframework.core.io.AbstractResource;

import java.io.*;
import java.util.Map;

/**
 * 利用模板引擎生成Spring Resource.
 *
 * @author sunhao(sunhao.java @ gmail.com)
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
    protected Map<String, Object> context;
    /**
     * 描述，即来源
     */
    protected String description;

    protected AbstractTemplateResource(String template, Map<String, Object> context) {
        this.template = template;
        this.context = context;
    }

    AbstractTemplateResource(String template, Map<String, Object> context, String description) {
        this.template = template;
        this.context = context;
        this.description = description == null ? "from template" : description;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(renderTemplate().getBytes("UTF-8"));
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    /**
     * 渲染模板
     *
     * @return 渲染后的内容
     */
    public abstract String renderTemplate();
}

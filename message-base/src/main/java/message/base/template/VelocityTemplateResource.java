package message.base.template;

import message.utils.FileUtils;
import message.utils.StringUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.*;
import java.util.Map;

/**
 * Velocity模板引擎生成的Resource<br/>.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 14-9-1 上午12:18
 */
public class VelocityTemplateResource extends AbstractTemplateResource {
    public VelocityTemplateResource(String template, Map<String, String> context) {
        super(template, context, "resource load by Velocity!");
    }

    @Override
    public String renderTemplate() {
        String result = StringUtils.EMPTY;

        InputStream is = null;
        try {
            is = FileUtils.openInputStream(new File(super.template));

            VelocityContext context = buildContext();
            StringWriter sw = new StringWriter();
            Reader reader = new InputStreamReader(is, "UTF-8");
            Velocity.evaluate(context, sw, super.template, reader);

            result = sw.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(is != null) {
                IOUtils.closeQuietly(is);
            }
        }

        return result;
    }

    private VelocityContext buildContext(){
        VelocityContext context = new VelocityContext();

        if(MapUtils.isNotEmpty(super.context)) {
            for(Map.Entry<String, ?> entry : super.context.entrySet()) {
                context.put(entry.getKey(), entry.getValue());
            }
        }

        return context;
    }
}

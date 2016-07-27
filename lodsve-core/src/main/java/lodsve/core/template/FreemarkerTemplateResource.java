package lodsve.core.template;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import lodsve.core.utils.FileUtils;
import lodsve.core.utils.StringUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Locale;
import java.util.Map;

/**
 * Freemarker模板引擎生成的Resource<br/>.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 14-9-1 上午12:18
 */
public class FreemarkerTemplateResource extends AbstractTemplateResource {
    public FreemarkerTemplateResource(String template, Map<String, String> context) {
        super(template, context, "resource load by Freemarker!");
    }

    @Override
    public String renderTemplate() {
        String result = StringUtils.EMPTY;

        StringWriter stringWriter = new StringWriter();
        BufferedWriter writer = new BufferedWriter(stringWriter);
        try {
            Configuration conf = new Configuration();
            //设置编码
            conf.setEncoding(Locale.getDefault(), "UTF-8");
            conf.setDirectoryForTemplateLoading(this.getFolder());	//加载freemarker模板文件
            conf.setObjectWrapper(new DefaultObjectWrapper());	//设置对象包装器
            conf.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);	//设计异常处理器

            Template template = conf.getTemplate(this.getFileName());

            //设置编码
            template.setEncoding("UTF-8");
            //最后开始生成
            template.process(super.context, writer);

            result = stringWriter.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                writer.flush();
                writer.close();
            } catch (IOException e) {

            }
        }

        return result;
    }

    private File getFolder(){
        return new File(super.template).getParentFile();
    }

    private String getFileName(){
        File file = new File(super.template);

        return FileUtils.getFileName(file) + "." + FileUtils.getFileExt(file);
    }
}

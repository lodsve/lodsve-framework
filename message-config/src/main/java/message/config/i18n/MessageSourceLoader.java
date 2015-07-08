package message.config.i18n;

import message.config.core.InitConfigPath;
import message.utils.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * i18n资源文件加载器
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 13-4-15 下午10:19
 */
public class MessageSourceLoader implements InitializingBean {
    /**
     * 加载了所有的资源文件信息.
     */
    private ResourceBundleHolder resourceBundleHolder;
    /**
     * 加载顺序
     */
    private int order;

    public void afterPropertiesSet() throws Exception {
        Resource[] resources = getResources();

        for (Resource r : resources) {
            String filePath = r.getFile().getAbsolutePath();
            if (StringUtils.indexOf(filePath, "_") == -1)
                this.resourceBundleHolder.loadMessageResource(filePath, this.order);
        }
    }

    private Resource[] getResources() throws IOException {
        String paramsRoot = InitConfigPath.getParamsRoot();

        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] propertiesResources = resolver.getResources("file:" + paramsRoot + "/i18n/**/*.properties");
        Resource[] htmlResources = resolver.getResources("file:" + paramsRoot + "/i18n/**/*.html");
        Resource[] txtResources = resolver.getResources("file:" + paramsRoot + "/i18n/**/*.txt");

        List<Resource> resources = new ArrayList<Resource>();
        resources.addAll(Arrays.asList(propertiesResources));
        resources.addAll(Arrays.asList(htmlResources));
        resources.addAll(Arrays.asList(txtResources));

        return resources.toArray(new Resource[]{});
    }

    public void setResourceBundleHolder(ResourceBundleHolder resourceBundleHolder) {
        this.resourceBundleHolder = resourceBundleHolder;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}

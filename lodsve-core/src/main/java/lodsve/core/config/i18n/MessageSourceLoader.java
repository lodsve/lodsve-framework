package lodsve.core.config.i18n;

import lodsve.core.config.core.ParamsHomeListener;
import lodsve.core.utils.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

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
@Component
public class MessageSourceLoader implements InitializingBean {
    /**
     * 加载了所有的资源文件信息.
     */
    @Autowired
    private ResourceBundleHolder resourceBundleHolder;

    public void afterPropertiesSet() throws Exception {
        Resource[] resources = getResources();

        for (Resource r : resources) {
            String filePath = r.getFile().getAbsolutePath();
            if (StringUtils.indexOf(filePath, "_") == -1)
                this.resourceBundleHolder.loadMessageResource(filePath, 1);
        }
    }

    private Resource[] getResources() throws IOException {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] propertiesResources = resolver.getResources("file:" + ParamsHomeListener.getParamsRoot() + "/i18n/**/*.properties");
        Resource[] htmlResources = resolver.getResources("file:" + ParamsHomeListener.getParamsRoot() + "/i18n/**/*.html");
        Resource[] txtResources = resolver.getResources("file:" + ParamsHomeListener.getParamsRoot() + "/i18n/**/*.txt");

        List<Resource> resources = new ArrayList<>();
        resources.addAll(Arrays.asList(propertiesResources));
        resources.addAll(Arrays.asList(htmlResources));
        resources.addAll(Arrays.asList(txtResources));

        return resources.toArray(new Resource[]{});
    }
}

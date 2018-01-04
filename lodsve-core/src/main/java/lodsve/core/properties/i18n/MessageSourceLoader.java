package lodsve.core.properties.i18n;

import lodsve.core.io.support.LodsvePathMatchingResourcePatternResolver;
import lodsve.core.properties.init.ParamsHome;
import lodsve.core.utils.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * i18n资源文件加载器
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 13-4-15 下午10:19
 */
@Component
public class MessageSourceLoader implements InitializingBean {
    /**
     * 加载了所有的资源文件信息.
     */
    @Autowired
    private ResourceBundleHolder resourceBundleHolder;

    @Override
    public void afterPropertiesSet() throws Exception {
        Resource[] resources = getResources();

        for (Resource r : resources) {
            String filePath = r.getFile().getAbsolutePath();
            if (StringUtils.indexOf(filePath, "_") == -1) {
                this.resourceBundleHolder.loadMessageResource(filePath, 1);
            }
        }
    }

    private Resource[] getResources() {

        ResourcePatternResolver resolver = new LodsvePathMatchingResourcePatternResolver();
        Resource[] propertiesResources;
        try {
            propertiesResources = resolver.getResources(ParamsHome.getInstance().getParamsRoot() + "/i18n/**/*.properties");
        } catch (IOException e) {
            propertiesResources = new Resource[0];
        }
        Resource[] htmlResources;
        try {
            htmlResources = resolver.getResources(ParamsHome.getInstance().getParamsRoot() + "/i18n/**/*.html");
        } catch (IOException e) {
            htmlResources = new Resource[0];
        }
        Resource[] txtResources;
        try {
            txtResources = resolver.getResources(ParamsHome.getInstance().getParamsRoot() + "/i18n/**/*.txt");
        } catch (IOException e) {
            txtResources = new Resource[0];
        }

        List<Resource> resources = new ArrayList<>();
        resources.addAll(Arrays.asList(propertiesResources));
        resources.addAll(Arrays.asList(htmlResources));
        resources.addAll(Arrays.asList(txtResources));

        return resources.toArray(new Resource[resources.size()]);
    }
}

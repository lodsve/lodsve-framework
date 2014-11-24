package message.config.i18n;

import static message.config.core.InitConfigPath.PARAMS_ROOT;

import message.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.*;

/**
 * i18n资源文件加载器,<br/>
 * 默认先加载/WEB-INF/i18n/下的文件，再加载注入目录的i18n属性文件，后者覆盖前者.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 13-4-15 下午10:19
 */
public class MessageSourceLoader implements InitializingBean {
    /**
     * for logger
     */
    private static final Logger logger = LoggerFactory.getLogger(MessageSourceLoader.class);
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
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] propertiesResources = resolver.getResources("file:" + PARAMS_ROOT + "/i18n/**/*.properties");
        Resource[] htmlResources = resolver.getResources("file:" + PARAMS_ROOT + "/i18n/**/*.html");
        Resource[] txtResources = resolver.getResources("file:" + PARAMS_ROOT + "/i18n/**/*.txt");

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

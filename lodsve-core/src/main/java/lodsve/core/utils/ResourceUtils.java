package lodsve.core.utils;

import lodsve.core.io.ZookeeperResource;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import java.io.IOException;

/**
 * resource utils.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2018/1/6 上午1:34
 */
public final class ResourceUtils {
    private ResourceUtils() {
    }

    public static String getPath(Resource resource) {
        Assert.notNull(resource);

        try {
            String pathToUse;
            if (resource instanceof ClassPathResource) {
                pathToUse = ((ClassPathResource) resource).getPath();
            } else if (resource instanceof FileSystemResource) {
                pathToUse = resource.getFile().getAbsolutePath();
            } else if (resource instanceof ZookeeperResource) {
                pathToUse = "zookeeper:" + ((ZookeeperResource) resource).getPath();
            } else {
                pathToUse = resource.getURL().getPath();
            }

            return pathToUse;
        } catch (IOException e) {
            return "";
        }
    }

    public static String getResourceProtocol(Resource resource) {
        Assert.notNull(resource);

        String protocol;
        if (resource instanceof ClassPathResource) {
            protocol = "classpath:";
        } else if (resource instanceof FileSystemResource) {
            protocol = "file:";
        } else if (resource instanceof ZookeeperResource) {
            protocol = "zookeeper:";
        } else {
            protocol = "";
        }

        return protocol;
    }

    public static String getContent(Resource resource, String fileEncoding) {
        Assert.notNull(resource);

        try {
            return IOUtils.toString(resource.getInputStream(), StringUtils.isBlank(fileEncoding) ? "UTF-8" : fileEncoding);
        } catch (IOException e) {
            return "";
        }
    }

    public static String getContent(Resource resource) {
        return getContent(resource, "");
    }
}

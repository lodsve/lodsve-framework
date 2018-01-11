package lodsve.core;

import lodsve.core.utils.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.Manifest;

/**
 * 获取框架版本号，即maven中的version.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version 1.0 2018/1/11 下午11:07
 */
public final class LodsveVersion {
    private LodsveVersion() {
    }

    public static String getVersion() {
        return LodsveVersion.class.getPackage().getImplementationVersion();
    }

    public static String getBuilter() {
        return getManifestAttr("Built-By");
    }

    public static String getManifestAttr(String name) {
        InputStream inputStream = null;
        try {
            ClassLoader classLoader = LodsveVersion.class.getClassLoader();
            Enumeration<URL> manifestResources = classLoader.getResources("META-INF/MANIFEST.MF");
            while (manifestResources.hasMoreElements()) {
                inputStream = manifestResources.nextElement().openStream();
                Manifest manifest = new Manifest(inputStream);
                String builter = manifest.getMainAttributes().getValue(name);

                if (StringUtils.isNotBlank(builter)) {
                    return builter;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return StringUtils.EMPTY;
    }
}

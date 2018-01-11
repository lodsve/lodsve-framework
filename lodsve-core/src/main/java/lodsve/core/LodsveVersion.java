package lodsve.core;

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
}

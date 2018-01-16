package lodsve;

import lodsve.core.LodsveVersion;

/**
 * Lodsve.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version 1.0 2018/1/16 下午9:47
 */
public class Lodsve {
    private Lodsve() {
    }

    /**
     * 获取当前版本号
     *
     * @return 当前版本号
     * @see LodsveVersion#getVersion()
     */
    public static String getVersion() {
        return LodsveVersion.getVersion();
    }
}

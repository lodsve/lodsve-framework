package lodsve.cache.annotations;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/4/1 上午10:52
 */
public final class CacheModeHolder {
    private static CacheMode cacheMode;

    public static CacheMode getCacheMode() {
        return CacheModeHolder.cacheMode;
    }

    protected static void setCacheMode(CacheMode cacheMode) {
        CacheModeHolder.cacheMode = cacheMode;
    }
}

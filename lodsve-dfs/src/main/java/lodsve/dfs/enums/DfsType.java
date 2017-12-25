package lodsve.dfs.enums;

import lodsve.dfs.service.DfsService;
import lodsve.dfs.service.impl.FastDfsServiceImpl;
import lodsve.dfs.service.impl.GoogleFsServiceImpl;
import lodsve.dfs.service.impl.TFsServiceImpl;

/**
 * 使用的文件系统类型.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2017-12-4-0004 10:41
 */
public enum DfsType {
    /**
     * fast_dfs
     */
    FAST_DFS(FastDfsServiceImpl.class),
    /**
     * tfs
     */
    TFS(TFsServiceImpl.class),
    /**
     * GoogleFS
     */
    GOOGLE_FS(GoogleFsServiceImpl.class);

    private Class<? extends DfsService> implClazz;

    DfsType(Class<? extends DfsService> implClazz) {
        this.implClazz = implClazz;
    }

    public Class<? extends DfsService> getImplClazz() {
        return implClazz;
    }
}

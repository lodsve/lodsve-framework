package lodsve.fs.enums;

import lodsve.fs.service.FsService;
import lodsve.fs.service.impl.FastDfsServiceImpl;
import lodsve.fs.service.impl.GoogleFsServiceImpl;
import lodsve.fs.service.impl.TFsServiceImpl;

/**
 * 使用的文件系统类型.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2017-12-4-0004 10:41
 */
public enum FsType {
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

    private Class<? extends FsService> implClazz;

    FsType(Class<? extends FsService> implClazz) {
        this.implClazz = implClazz;
    }

    public Class<? extends FsService> getImplClazz() {
        return implClazz;
    }
}

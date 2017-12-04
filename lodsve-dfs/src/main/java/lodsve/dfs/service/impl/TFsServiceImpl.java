package lodsve.dfs.service.impl;

import lodsve.dfs.service.FsService;

import java.io.File;

/**
 * tfs.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2017-12-4-0004 13:20
 */
public class TFsServiceImpl implements FsService {
    @Override
    public String upload(byte[] fileBytes, String suffix) {
        return null;
    }

    @Override
    public String upload(File file) {
        return null;
    }

    @Override
    public String getUrl(String filePath) {
        return null;
    }

    @Override
    public byte[] download(String filePath) {
        return new byte[0];
    }

    @Override
    public boolean delete(String filePath) {
        return false;
    }
}

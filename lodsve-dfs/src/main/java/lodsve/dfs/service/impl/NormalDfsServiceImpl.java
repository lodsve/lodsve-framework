/*
 * Copyright (C) 2018  Sun.Hao
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package lodsve.dfs.service.impl;

import lodsve.core.utils.DateUtils;
import lodsve.core.utils.EncryptUtils;
import lodsve.dfs.configuration.DfsProperties;
import lodsve.dfs.service.DfsService;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 保存在本地服务器.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @date 2018-1-24-0024 12:42
 */
public class NormalDfsServiceImpl implements DfsService {
    @Autowired
    private DfsProperties fsProperties;
    private String storePath;

    @PostConstruct
    public void init() {
        storePath = StringUtils.cleanPath(fsProperties.getNormal().getStore());
        if (!new File(storePath).exists()) {
            new File(storePath).mkdirs();
        }
    }

    @Override
    public String upload(byte[] fileBytes, String suffix) {
        String path = makePath(EncryptUtils.getFileMD5(fileBytes)) + "." + suffix;
        try {
            IOUtils.copy(new ByteArrayInputStream(fileBytes), new FileOutputStream(new File(path)));
        } catch (IOException e) {
            return "";
        }

        return lodsve.core.utils.StringUtils.remove(path, storePath);
    }

    @Override
    public String upload(File file) {
        try {
            return upload(IOUtils.toByteArray(new FileInputStream(file)), FilenameUtils.getExtension(file.getName()));
        } catch (IOException e) {
            return "";
        }
    }

    @Override
    public String getUrl(String filePath) {
        return StringUtils.cleanPath(storePath + "/" + filePath);
    }

    @Override
    public byte[] download(String filePath) {
        try {
            return IOUtils.toByteArray(new FileInputStream(new File(StringUtils.cleanPath(storePath + "/" + filePath))));
        } catch (IOException e) {
            return new byte[0];
        }
    }

    @Override
    public boolean delete(String filePath) {
        File file = new File(StringUtils.cleanPath(storePath + "/" + filePath));
        return file.exists() && file.isFile() && file.canRead() && file.delete();

    }

    private String makePath(String contentMd5) {
        Date now = DateUtils.getNow();
        String path = new SimpleDateFormat("/yyyy/MM/dd/").format(now);
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }

        return StringUtils.cleanPath(storePath + "/" + path + "/" + contentMd5);
    }
}

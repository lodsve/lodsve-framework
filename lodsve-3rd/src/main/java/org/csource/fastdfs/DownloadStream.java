/*
 * Copyright (C) 2019 Sun.Hao(https://www.crazy-coder.cn/)
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

package org.csource.fastdfs;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Download file by stream (download callback class)
 *
 * @author zhouzezhong & Happy Fish / YuQing
 * @version Version 1.11
 */
public class DownloadStream implements DownloadCallback {
    private OutputStream out;
    private long currentBytes = 0;

    public DownloadStream(OutputStream out) {
        super();
        this.out = out;
    }

    /**
     * recv file content callback function, may be called more than once when the file downloaded
     *
     * @param fileSize file size
     * @param data     data buff
     * @param bytes    data bytes
     * @return 0 success, return none zero(errno) if fail
     */
    public int recv(long fileSize, byte[] data, int bytes) {
        try {
            out.write(data, 0, bytes);
        } catch (IOException ex) {
            ex.printStackTrace();
            return -1;
        }

        currentBytes += bytes;
        if (this.currentBytes == fileSize) {
            this.currentBytes = 0;
        }

        return 0;
    }
}

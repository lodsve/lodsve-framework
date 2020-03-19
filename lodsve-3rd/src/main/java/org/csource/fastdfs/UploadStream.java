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
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Upload file by stream
 *
 * @author zhouzezhong & Happy Fish / YuQing
 * @version Version 1.11
 */
public class UploadStream implements UploadCallback {
    private InputStream inputStream; //input stream for reading
    private long fileSize = 0;  //size of the uploaded file

    /**
     * constructor
     *
     * @param inputStream input stream for uploading
     * @param fileSize    size of uploaded file
     */
    public UploadStream(InputStream inputStream, long fileSize) {
        super();
        this.inputStream = inputStream;
        this.fileSize = fileSize;
    }

    /**
     * send file content callback function, be called only once when the file uploaded
     *
     * @param out output stream for writing file content
     * @return 0 success, return none zero(errno) if fail
     */
    public int send(OutputStream out) throws IOException {
        long remainBytes = fileSize;
        byte[] buff = new byte[256 * 1024];
        int bytes;
        while (remainBytes > 0) {
            try {
                if ((bytes = inputStream.read(buff, 0, remainBytes > buff.length ? buff.length : (int) remainBytes)) < 0) {
                    return -1;
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                return -1;
            }

            out.write(buff, 0, bytes);
            remainBytes -= bytes;
        }

        return 0;
    }
}

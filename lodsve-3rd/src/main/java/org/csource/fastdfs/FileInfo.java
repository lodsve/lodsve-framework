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

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Server Info
 * @author Happy Fish / YuQing
 * @version Version 1.23
 */
public class FileInfo {
    protected String source_ip_addr;
    protected long file_size;
    protected Date create_timestamp;
    protected int crc32;

    /**
     * Constructor
     * @param file_size the file size
     * @param create_timestamp create timestamp in seconds
     * @param crc32 the crc32 signature
     * @param source_ip_addr the source storage ip address
     */
    public FileInfo(long file_size, int create_timestamp, int crc32, String source_ip_addr) {
        this.file_size = file_size;
        this.create_timestamp = new Date(create_timestamp * 1000L);
        this.crc32 = crc32;
        this.source_ip_addr = source_ip_addr;
    }

    /**
     * set the source ip address of the file uploaded to
     * @param source_ip_addr the source ip address
     */
    public void setSourceIpAddr(String source_ip_addr) {
        this.source_ip_addr = source_ip_addr;
    }

    /**
     * get the source ip address of the file uploaded to
     * @return the source ip address of the file uploaded to
     */
    public String getSourceIpAddr() {
        return this.source_ip_addr;
    }

    /**
     * set the file size
     * @param file_size the file size
     */
    public void setFileSize(long file_size) {
        this.file_size = file_size;
    }

    /**
     * get the file size
     * @return the file size
     */
    public long getFileSize() {
        return this.file_size;
    }

    /**
     * set the create timestamp of the file
     * @param create_timestamp create timestamp in seconds
     */
    public void setCreateTimestamp(int create_timestamp) {
        this.create_timestamp = new Date(create_timestamp * 1000L);
    }

    /**
     * get the create timestamp of the file
     * @return the create timestamp of the file
     */
    public Date getCreateTimestamp() {
        return this.create_timestamp;
    }

    /**
     * set the create timestamp of the file
     * @param crc32 the crc32 signature
     */
    public void setCrc32(int crc32) {
        this.crc32 = crc32;
    }

    /**
     * get the file CRC32 signature
     * @return the file CRC32 signature
     */
    public long getCrc32() {
        return this.crc32;
    }

    /**
     * to string
     * @return string
     */
    public String toString() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return "source_ip_addr = " + this.source_ip_addr + ", " +
                "file_size = " + this.file_size + ", " +
                "create_timestamp = " + df.format(this.create_timestamp) + ", " +
                "crc32 = " + this.crc32;
    }
}

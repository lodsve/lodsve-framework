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

/**
 * Download file callback interface
 * @author Happy Fish / YuQing
 * @version Version 1.4
 */
public interface DownloadCallback {
    /**
     * recv file content callback function, may be called more than once when the file downloaded
     * @param file_size file size
     *	@param data data buff
     * @param bytes data bytes
     * @return 0 success, return none zero(errno) if fail
     */
    public int recv(long file_size, byte[] data, int bytes);
}

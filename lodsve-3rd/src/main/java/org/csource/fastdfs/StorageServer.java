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
import java.net.InetSocketAddress;

/**
 * Storage Server Info
 * @author Happy Fish / YuQing
 * @version Version 1.11
 */
public class StorageServer extends TrackerServer {
    protected int store_path_index = 0;

    /**
     * Constructor
     * @param ip_addr the ip address of storage server
     * @param port the port of storage server
     * @param store_path the store path index on the storage server
     */
    public StorageServer(String ip_addr, int port, int store_path) throws IOException {
        super(ClientGlobal.getSocket(ip_addr, port), new InetSocketAddress(ip_addr, port));
        this.store_path_index = store_path;
    }

    /**
     * Constructor
     * @param ip_addr the ip address of storage server
     * @param port the port of storage server
     * @param store_path the store path index on the storage server
     */
    public StorageServer(String ip_addr, int port, byte store_path) throws IOException {
        super(ClientGlobal.getSocket(ip_addr, port), new InetSocketAddress(ip_addr, port));
        if (store_path < 0) {
            this.store_path_index = 256 + store_path;
        } else {
            this.store_path_index = store_path;
        }
    }

    /**
     * @return the store path index on the storage server
     */
    public int getStorePathIndex() {
        return this.store_path_index;
    }
}

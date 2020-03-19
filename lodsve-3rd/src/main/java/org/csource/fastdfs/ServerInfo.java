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
import java.net.Socket;

/**
 * Server Info
 * @author Happy Fish / YuQing
 * @version Version 1.7
 */
public class ServerInfo {
    protected String ip_addr;
    protected int port;

    /**
     * Constructor
     * @param ip_addr address of the server
     * @param port the port of the server
     */
    public ServerInfo(String ip_addr, int port) {
        this.ip_addr = ip_addr;
        this.port = port;
    }

    /**
     * return the ip address
     * @return the ip address
     */
    public String getIpAddr() {
        return this.ip_addr;
    }

    /**
     * return the port of the server
     * @return the port of the server
     */
    public int getPort() {
        return this.port;
    }

    /**
     * connect to server
     * @return connected Socket object
     */
    public Socket connect() throws IOException {
        Socket sock = new Socket();
        sock.setReuseAddress(true);
        sock.setSoTimeout(ClientGlobal.g_network_timeout);
        sock.connect(new InetSocketAddress(this.ip_addr, this.port), ClientGlobal.g_connect_timeout);
        return sock;
    }
}

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
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Tracker Server Info
 * @author Happy Fish / YuQing
 * @version Version 1.11
 */
public class TrackerServer {
    protected Socket sock;
    protected InetSocketAddress inetSockAddr;

    /**
     * Constructor
     * @param sock Socket of server
     * @param inetSockAddr the server info
     */
    public TrackerServer(Socket sock, InetSocketAddress inetSockAddr) {
        this.sock = sock;
        this.inetSockAddr = inetSockAddr;
    }

    /**
     * get the connected socket
     * @return the socket
     */
    public Socket getSocket() throws IOException {
        if (this.sock == null) {
            this.sock = ClientGlobal.getSocket(this.inetSockAddr);
        }

        return this.sock;
    }

    /**
     * get the server info
     * @return the server info
     */
    public InetSocketAddress getInetSocketAddress() {
        return this.inetSockAddr;
    }

    public OutputStream getOutputStream() throws IOException {
        return this.sock.getOutputStream();
    }

    public InputStream getInputStream() throws IOException {
        return this.sock.getInputStream();
    }

    public void close() throws IOException {
        if (this.sock != null) {
            try {
                ProtoCommon.closeSocket(this.sock);
            } finally {
                this.sock = null;
            }
        }
    }

    protected void finalize() throws Throwable {
        this.close();
    }
}

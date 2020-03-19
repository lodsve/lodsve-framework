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
 * Tracker server group
 * @author Happy Fish / YuQing
 * @version Version 1.17
 */
public class TrackerGroup {
    protected Integer lock;
    public int tracker_server_index;
    public InetSocketAddress[] tracker_servers;

    /**
     * Constructor
     * @param tracker_servers tracker servers
     */
    public TrackerGroup(InetSocketAddress[] tracker_servers) {
        this.tracker_servers = tracker_servers;
        this.lock = new Integer(0);
        this.tracker_server_index = 0;
    }

    /**
     * return connected tracker server
     * @return connected tracker server, null for fail
     */
    public TrackerServer getConnection(int serverIndex) throws IOException {
        Socket sock = new Socket();
        sock.setReuseAddress(true);
        sock.setSoTimeout(ClientGlobal.g_network_timeout);
        sock.connect(this.tracker_servers[serverIndex], ClientGlobal.g_connect_timeout);
        return new TrackerServer(sock, this.tracker_servers[serverIndex]);
    }

    /**
     * return connected tracker server
     * @return connected tracker server, null for fail
     */
    public TrackerServer getConnection() throws IOException {
        int current_index;

        synchronized (this.lock) {
            this.tracker_server_index++;
            if (this.tracker_server_index >= this.tracker_servers.length) {
                this.tracker_server_index = 0;
            }

            current_index = this.tracker_server_index;
        }

        try {
            return this.getConnection(current_index);
        } catch (IOException ex) {
            System.err.println("connect to server " + this.tracker_servers[current_index].getAddress().getHostAddress() + ":" + this.tracker_servers[current_index].getPort() + " fail");
            ex.printStackTrace(System.err);
        }

        for (int i = 0; i < this.tracker_servers.length; i++) {
            if (i == current_index) {
                continue;
            }

            try {
                TrackerServer trackerServer = this.getConnection(i);

                synchronized (this.lock) {
                    if (this.tracker_server_index == current_index) {
                        this.tracker_server_index = i;
                    }
                }

                return trackerServer;
            } catch (IOException ex) {
                System.err.println("connect to server " + this.tracker_servers[i].getAddress().getHostAddress() + ":" + this.tracker_servers[i].getPort() + " fail");
                ex.printStackTrace(System.err);
            }
        }

        return null;
    }

    public Object clone() {
        InetSocketAddress[] trackerServers = new InetSocketAddress[this.tracker_servers.length];
        for (int i = 0; i < trackerServers.length; i++) {
            trackerServers[i] = new InetSocketAddress(this.tracker_servers[i].getAddress().getHostAddress(), this.tracker_servers[i].getPort());
        }

        return new TrackerGroup(trackerServers);
    }
}

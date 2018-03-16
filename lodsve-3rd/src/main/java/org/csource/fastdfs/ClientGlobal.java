/**
 * Copyright (C) 2008 Happy Fish / YuQing
 * <p>
 * FastDFS Java Client may be copied only under the terms of the GNU Lesser
 * General Public License (LGPL).
 * Please visit the FastDFS Home Page http://www.csource.org/ for more detail.
 **/

package org.csource.fastdfs;

import org.csource.common.FastDfsConfig;
import org.csource.common.FsException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Global variables
 *
 * @author Happy Fish / YuQing
 * @version Version 1.11
 */
public class ClientGlobal {
    /**
     * millisecond
     */
    public static int g_connect_timeout;
    /**
     * millisecond
     */
    public static int g_network_timeout;
    public static String g_charset;
    public static int g_tracker_http_port;
    /**
     * if anti-steal token
     */
    public static boolean g_anti_steal_token;
    /**
     * generage token secret key
     */
    public static String g_secret_key;
    public static TrackerGroup g_tracker_group;
    /**
     * second
     */
    public static final int DEFAULT_CONNECT_TIMEOUT = 5;
    /**
     * second
     */
    public static final int DEFAULT_NETWORK_TIMEOUT = 30;

    private ClientGlobal() {
    }

    /**
     * load global variables
     *
     * @param config config
     */
    public static void init(FastDfsConfig config) throws FsException {
        String[] szTrackerServers;
        String[] parts;

        g_connect_timeout = config.getConnectTimeout();
        if (g_connect_timeout < 0) {
            g_connect_timeout = DEFAULT_CONNECT_TIMEOUT;
        }
        // millisecond
        g_connect_timeout *= 1000;

        g_network_timeout = config.getNetworkTimeout();
        if (g_network_timeout < 0) {
            g_network_timeout = DEFAULT_NETWORK_TIMEOUT;
        }
        // millisecond
        g_network_timeout *= 1000;

        g_charset = config.getCharset();
        if (g_charset == null || g_charset.length() == 0) {
            g_charset = "ISO8859-1";
        }

        szTrackerServers = config.getServers();
        if (szTrackerServers == null) {
            throw new FsException("item \"tracker_server\" in \"/spring-config/fdfs_client.conf\" not found");
        }

        InetSocketAddress[] trackerServers = new InetSocketAddress[szTrackerServers.length];
        for (int i = 0; i < szTrackerServers.length; i++) {
            parts = szTrackerServers[i].split("\\:", 2);
            if (parts.length != 2) {
                throw new FsException("the value of item \"tracker_server\" is invalid, the correct format is host:port");
            }

            trackerServers[i] = new InetSocketAddress(parts[0].trim(), Integer.parseInt(parts[1].trim()));
        }
        g_tracker_group = new TrackerGroup(trackerServers);

        g_tracker_http_port = config.getTrackerHttpPort();
        g_anti_steal_token = config.isAntiStealToken();
        if (g_anti_steal_token) {
            g_secret_key = config.getSecretKey();
        }
    }

    /**
     * construct Socket object
     *
     * @param ip_addr ip address or hostname
     * @param port    port number
     * @return connected Socket object
     */
    public static Socket getSocket(String ip_addr, int port) throws IOException {
        Socket sock = new Socket();
        sock.setSoTimeout(ClientGlobal.g_network_timeout);
        sock.connect(new InetSocketAddress(ip_addr, port), ClientGlobal.g_connect_timeout);
        return sock;
    }

    /**
     * construct Socket object
     *
     * @param addr InetSocketAddress object, including ip address and port
     * @return connected Socket object
     */
    public static Socket getSocket(InetSocketAddress addr) throws IOException {
        Socket sock = new Socket();
        sock.setSoTimeout(ClientGlobal.g_network_timeout);
        sock.connect(addr, ClientGlobal.g_connect_timeout);
        return sock;
    }

    public static int getG_connect_timeout() {
        return g_connect_timeout;
    }

    public static void setG_connect_timeout(int connect_timeout) {
        ClientGlobal.g_connect_timeout = connect_timeout;
    }

    public static int getG_network_timeout() {
        return g_network_timeout;
    }

    public static void setG_network_timeout(int network_timeout) {
        ClientGlobal.g_network_timeout = network_timeout;
    }

    public static String getG_charset() {
        return g_charset;
    }

    public static void setG_charset(String charset) {
        ClientGlobal.g_charset = charset;
    }

    public static int getG_tracker_http_port() {
        return g_tracker_http_port;
    }

    public static void setG_tracker_http_port(int tracker_http_port) {
        ClientGlobal.g_tracker_http_port = tracker_http_port;
    }

    public static boolean getG_anti_steal_token() {
        return g_anti_steal_token;
    }

    public static boolean isG_anti_steal_token() {
        return g_anti_steal_token;
    }

    public static void setG_anti_steal_token(boolean anti_steal_token) {
        ClientGlobal.g_anti_steal_token = anti_steal_token;
    }

    public static String getG_secret_key() {
        return g_secret_key;
    }

    public static void setG_secret_key(String secret_key) {
        ClientGlobal.g_secret_key = secret_key;
    }

    public static TrackerGroup getG_tracker_group() {
        return g_tracker_group;
    }

    public static void setG_tracker_group(TrackerGroup tracker_group) {
        ClientGlobal.g_tracker_group = tracker_group;
    }
}

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

package lodsve.test.mock.memcached;

import com.thimbleware.jmemcached.CacheImpl;
import com.thimbleware.jmemcached.Key;
import com.thimbleware.jmemcached.LocalCacheElement;
import com.thimbleware.jmemcached.MemCacheDaemon;
import com.thimbleware.jmemcached.storage.CacheStorage;
import com.thimbleware.jmemcached.storage.hash.ConcurrentLinkedHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * 内嵌式Memcached.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2018-1-10-0010 13:32
 */
public final class JMemcachedServer {

    private static Logger logger = LoggerFactory.getLogger(JMemcachedServer.class);

    private static final long DEFAULT_STARTUP_TIMEOUT = 10000;
    private static final int DEFAULT_STORAGE_CAPACITY = 1000;
    private static final long DEFAULT_STORAGE_MEMORY_CAPACITY = 10000;

    private MemCacheDaemon<LocalCacheElement> memcacheDaemon;

    public void start(final String host, final int port) {
        logger.debug("Starting memcache...");

        final CountDownLatch startupLatch = new CountDownLatch(1);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                CacheStorage<Key, LocalCacheElement> storage = ConcurrentLinkedHashMap.create(ConcurrentLinkedHashMap
                        .EvictionPolicy.FIFO, DEFAULT_STORAGE_CAPACITY, DEFAULT_STORAGE_MEMORY_CAPACITY);
                memcacheDaemon = new MemCacheDaemon<>();
                memcacheDaemon.setCache(new CacheImpl(storage));
                memcacheDaemon.setAddr(new InetSocketAddress(host, port));
                memcacheDaemon.start();
                startupLatch.countDown();
            }
        });
        try {
            if (!startupLatch.await(DEFAULT_STARTUP_TIMEOUT, MILLISECONDS)) {
                logger.error("Memcache daemon did not start after {}ms. Consider increasing the timeout", MILLISECONDS);
                throw new AssertionError("Memcache daemon did not start within timeout");
            }
        } catch (InterruptedException e) {
            logger.error("Interrupted waiting for Memcache daemon to start:", e);
            throw new AssertionError(e);
        }
    }

    public void clean() {
        if (memcacheDaemon != null) {
            logger.debug("Cleaning memcache...");
            memcacheDaemon.getCache().flush_all();
        }
    }
}

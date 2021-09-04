/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

    private static final Logger logger = LoggerFactory.getLogger(JMemcachedServer.class);

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

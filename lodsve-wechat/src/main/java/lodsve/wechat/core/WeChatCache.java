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
package lodsve.wechat.core;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.lang.NonNull;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 微信缓存.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 */
public class WeChatCache {
    private final Cache<String, Object> CACHE = CacheBuilder.newBuilder().expireAfterWrite(90, TimeUnit.MINUTES).build();

    /**
     * 获取值，如果值在缓存中不存在，则调用{@link Callable#call()}获取值，并且保存到缓存中
     *
     * @param key      缓存中的key
     * @param callable 回调函数，用来获取值，然后保存到缓存中
     * @param <T>      值得类型
     * @return 缓存中的值
     * @throws ExecutionException 获取缓存异常
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key, @NonNull Callable<T> callable) throws ExecutionException {
        return (T) CACHE.get(key, callable);
    }
}

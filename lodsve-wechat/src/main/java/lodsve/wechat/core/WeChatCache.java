/*
 * Copyright © 2009 Sun.Hao(https://www.crazy-coder.cn/)
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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
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

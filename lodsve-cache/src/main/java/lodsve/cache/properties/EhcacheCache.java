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
package lodsve.cache.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * Ehcache Cache.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2018-4-25-0025 11:49
 */
@Setter
@Getter
public class EhcacheCache {
    private String name;
    private long maxElementsInMemory = 10000;
    private boolean eternal = false;
    private long timeToIdleSeconds = 300;
    private long timeToLiveSeconds = 600;
    private boolean overflowToDisk = true;
}

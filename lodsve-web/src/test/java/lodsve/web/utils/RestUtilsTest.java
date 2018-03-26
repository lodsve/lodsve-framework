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

package lodsve.web.utils;

import lodsve.web.utils.domain.Demo;
import org.junit.Assert;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * .
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @date 2018-3-26-0026 15:19
 */
public class RestUtilsTest {

    /**
     * @see RestUtils#get(URI, Class)
     */
    @Test
    public void get() throws URISyntaxException {
        // 测试具体返回值
        URI uri = new URI("http://localhost:8080/demo/demo/11");
        Demo demo = RestUtils.get(uri, Demo.class);
        Assert.assertNotNull(demo);
    }

    /**
     * @see RestUtils#get(String, Class, Object...)
     */
    @Test
    public void get1() {
    }

    /**
     * @see RestUtils#get(String, Class)
     */
    @Test
    public void get2() {
        // 测试void
        Assert.assertNull(RestUtils.get("http://localhost:8080/demo/void", Void.class));
    }

    /**
     * @see RestUtils#get(String, Class, Map)
     */
    @Test
    public void get3() {
    }

    @Test
    public void post() {
    }

    @Test
    public void post1() {
    }

    @Test
    public void post2() {
    }

    @Test
    public void post3() {
    }

    @Test
    public void head() {
    }

    @Test
    public void head1() {
    }

    @Test
    public void head2() {
    }

    @Test
    public void put() {
    }

    @Test
    public void put1() {
    }

    @Test
    public void put2() {
    }

    @Test
    public void delete() {
    }

    @Test
    public void delete1() {
    }

    @Test
    public void delete2() {
    }

    @Test
    public void options() {
    }

    @Test
    public void options1() {
    }

    @Test
    public void options2() {
    }
}
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

package lodsve.web.utils;

import org.junit.Assert;
import org.junit.Test;

/**
 * .
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2017-12-28-0028 15:59
 */
public class IpUtilsTest {
    @Test
    public void testGetAllInfo() {
        Assert.assertNotNull(IpUtils.getAllInfo("180.97.33.107"));
    }

    @Test
    public void testGetCountry(){
        Assert.assertNotNull(IpUtils.getCountry("180.97.33.107"));
    }

    @Test
    public void testGetInetIps(){
        Assert.assertNotNull(IpUtils.getInetIps());
    }

    @Test
    public void testGetInetIp(){
        Assert.assertNotNull(IpUtils.getInetIp());
    }
}

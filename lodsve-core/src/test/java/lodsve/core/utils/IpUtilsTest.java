package lodsve.core.utils;

import org.junit.Assert;
import org.junit.Test;

/**
 * .
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 2017-12-28-0028 15:59
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

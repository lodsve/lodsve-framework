package lodsve.core.propeties.init;

import lodsve.core.properties.ParamsHome;
import org.junit.Assert;
import org.junit.Test;

/**
 * .
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2017-12-27-0027 13:39
 */
public class ParamsHomeTest {

    @Test
    public void test01() {
        ParamsHome.getInstance().init("classpath:/META-INF/config-template");
        Assert.assertNotNull(ParamsHome.getInstance().getParamsRoot());
    }
}

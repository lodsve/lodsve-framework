package lodsve.core.propeties.init;

import lodsve.core.properties.init.ParamsHome;
import org.junit.Test;

/**
 * .
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 2017-12-27-0027 13:39
 */
public class ParamsHomeTest {

    @Test
    public void test01() {
        ParamsHome.getInstance().init("classpath:/META-INF/config-template");
        System.out.println(ParamsHome.getInstance().getParamsRoot());
    }
}

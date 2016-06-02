package lodsve.test.base;

import lodsve.core.context.ApplicationHelper;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 做单元测试的基类.<br/>
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 12-7-10 下午9:27
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:/META-INF/spring/*.xml")
public abstract class GenericTest extends AbstractJUnit4SpringContextTests {
    /**
     * 开始单元测试的时候先将applicationContext注入
     */
    @Before
    public void initTest() {
        ApplicationHelper.getInstance().addApplicationContext(applicationContext);
    }
}

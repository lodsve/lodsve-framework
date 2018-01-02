package lodsve.test.base;

import lodsve.core.properties.profile.ProfileInitializer;
import lodsve.test.context.LodsveTestExecutionListener;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 做单元测试的基类.<br/>
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 12-7-10 下午9:27
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/META-INF/spring/*.xml", initializers = ProfileInitializer.class)
@TestExecutionListeners(LodsveTestExecutionListener.class)
public class BaseTest extends AbstractJUnit4SpringContextTests {
}

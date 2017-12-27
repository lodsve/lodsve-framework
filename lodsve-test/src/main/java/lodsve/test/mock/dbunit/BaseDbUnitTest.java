/****************************************************************************************
 * Copyright © 2003-2012 ZTEsoft Corporation. All rights reserved. Reproduction or       <br>
 * transmission in whole or in part, in any form or by any means, electronic, mechanical <br>
 * or otherwise, is prohibited without the prior written consent of the copyright owner. <br>
 ****************************************************************************************/
package lodsve.test.mock.dbunit;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import lodsve.test.base.BaseTest;
import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

/**
 * 使用dbunit对数据库操作的单元测试.<br/>
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16-3-10 12:34
 */
@RunWith(SpringJUnit4ClassRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class
})
public class BaseDbUnitTest extends BaseTest {

}

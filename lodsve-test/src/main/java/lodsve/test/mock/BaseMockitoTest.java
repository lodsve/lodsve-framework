/**************************************************************************************** 
 Copyright © 2003-2012 ZTEsoft Corporation. All rights reserved. Reproduction or       <br>
 transmission in whole or in part, in any form or by any means, electronic, mechanical <br>
 or otherwise, is prohibited without the prior written consent of the copyright owner. <br>
 ****************************************************************************************/
package lodsve.test.mock;

import lodsve.test.base.BaseTest;
import org.junit.Before;
import org.mockito.MockitoAnnotations;

/**
 * 使用mock时的基类.<br/>
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16-3-10 12:34
 */
public class BaseMockitoTest extends BaseTest {
    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }
}
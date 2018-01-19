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

/**************************************************************************************** 
 Copyright © 2003-2012 ZTEsoft Corporation. All rights reserved. Reproduction or       <br>
 transmission in whole or in part, in any form or by any means, electronic, mechanical <br>
 or otherwise, is prohibited without the prior written consent of the copyright owner. <br>
 ****************************************************************************************/
package lodsve.test.mock.mockito;

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
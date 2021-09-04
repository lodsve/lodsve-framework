/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package lodsve.core.context;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * .
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2016/12/20 上午10:54
 */
public class SpringSupportTest extends SpringSupport {
    @Autowired
    private DemoService demoService;

    private void say() {
        demoService.say();
    }

    public static void main(String[] args) {
        SpringSupportTest test = new SpringSupportTest();
        test.say();

        DemoService demoService = ApplicationHelper.getInstance().getBean(DemoService.class);
        demoService.say();
    }

    @Override
    public String supportConfigLocation() {
        return "spring/application-context.xml";
    }
}

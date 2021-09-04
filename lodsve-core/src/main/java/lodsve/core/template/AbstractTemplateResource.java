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
package lodsve.core.template;

import org.springframework.core.io.AbstractResource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 利用模板引擎生成Spring Resource.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 14-8-30 上午1:04
 */
public abstract class AbstractTemplateResource extends AbstractResource {
    /**
     * 模板路径
     */
    protected String template;
    /**
     * 模板需要的参数
     */
    protected Map<String, Object> context;
    /**
     * 描述，即来源
     */
    protected String description;

    protected AbstractTemplateResource(String template, Map<String, Object> context) {
        this.template = template;
        this.context = context;
    }

    AbstractTemplateResource(String template, Map<String, Object> context, String description) {
        this.template = template;
        this.context = context;
        this.description = description == null ? "from template" : description;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(renderTemplate().getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    /**
     * 渲染模板
     *
     * @return 渲染后的内容
     */
    public abstract String renderTemplate();
}

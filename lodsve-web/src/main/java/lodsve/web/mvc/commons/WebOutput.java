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
package lodsve.web.mvc.commons;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * 封装HttpServletResponse
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2012年1月19日
 */
public class WebOutput {
    private HttpServletResponse response;

    /**
     * 构造函数
     *
     * @param response
     */
    public WebOutput(HttpServletResponse response) {
        this.response = response;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    /**
     * 设置cookie
     *
     * @param name   name
     * @param value  value
     * @param maxAge 存活时间
     */
    public void setCookie(String name, String value, int maxAge) {
        Cookie c = new Cookie(name, value);
        if (maxAge > 0) {
            c.setMaxAge(maxAge);
        }
        c.setPath("/");
        this.response.addCookie(c);
    }

    public void setContentType(String contentType) {
        this.response.setContentType(contentType);
    }

    public void setContentType(String contextType, String charset) {
        if (charset == null) {
            this.response.setContentType(contextType);
        } else {
            this.response.setContentType(contextType + "; charset=" + charset);
        }
    }
}

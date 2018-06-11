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

package lodsve.web.springfox.paths;

import lodsve.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import springfox.documentation.spring.web.paths.RelativePathProvider;

import javax.servlet.ServletContext;

/**
 * 处理swagger的路径.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 16/3/24 上午9:47
 */
public class SpringFoxPathProvider extends RelativePathProvider {
    private String prefix;
    private final ServletContext servletContext;

    @Autowired
    public SpringFoxPathProvider(ServletContext servletContext) {
        super(servletContext);
        this.servletContext = servletContext;
    }

    @Override
    protected String applicationPath() {
        String path = servletContext.getContextPath() + prefix;
        if (StringUtils.isEmpty(path)) {
            return ROOT;
        }

        return path;
    }

    @Override
    protected String getDocumentationPath() {
        return ROOT;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}

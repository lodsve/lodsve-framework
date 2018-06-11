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

package lodsve.web.webservice.properties;

import java.util.HashMap;
import java.util.Map;

/**
 * Servlet Config.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2018-4-25-0025 14:56
 */
public class ServletConfig {
    /**
     * Servlet init parameters to pass to Spring Web Services.
     */
    private Map<String, String> init = new HashMap<>();

    /**
     * Load on startup priority of the Spring Web Services servlet.
     */
    private int loadOnStartup = -1;

    public Map<String, String> getInit() {
        return this.init;
    }

    public void setInit(Map<String, String> init) {
        this.init = init;
    }

    public int getLoadOnStartup() {
        return this.loadOnStartup;
    }

    public void setLoadOnStartup(int loadOnStartup) {
        this.loadOnStartup = loadOnStartup;
    }
}

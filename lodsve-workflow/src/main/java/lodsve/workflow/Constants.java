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

package lodsve.workflow;

/**
 * Constants.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2015-11-18 17:33
 */
public final class Constants {
    private Constants() {
    }

    public static final String DEFAULT_WORKFLOW_XML_PATH = "classpath*:/workflow/*.xml";
    public static final String TAG_NODE = "node";
    public static final String TAG_INTERCEPTOR = "interceptor";
    public static final String TAG_URLS = "urls";
    public static final String TAG_UPDATE_URL = "update-url";
    public static final String TAG_VIEW_URL = "view-url";
    public static final String TAG_TO = "to";
    public static final String TAG_CONDITIONAL = "conditional";

    public static final String ATTR_TITLE = "title";
    public static final String ATTR_NAME = "name";
    public static final String ATTR_DOMAIN = "domain";
    public static final String ATTR_NODE = TAG_NODE;
    public static final String ATTR_URL = "url";
    public static final String ATTR_TYPE = "type";
    public static final String ATTR_CLASS = "class";
    public static final String ATTR_BEAN = "bean";
}

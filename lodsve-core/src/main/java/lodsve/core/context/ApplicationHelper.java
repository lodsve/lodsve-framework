/*
 * Copyright (C) 2019 Sun.Hao(https://www.crazy-coder.cn/)
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

package lodsve.core.context;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;

/**
 * applicationContext的辅助类
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2012-3-8 下午09:56:58
 */
public class ApplicationHelper {
    private static ApplicationHelper instance = new ApplicationHelper();

    private String rootPath;

    private String contextPath;

    private Set<ApplicationContext> apps = new HashSet<>();

    /**
     * 构造器私有，不可在外部进行初始化实例
     */
    private ApplicationHelper() {

    }

    /**
     * 根据class获取bean
     *
     * @param clazz type the bean must match; can be an interface or superclass. null is disallowed.
     * @param <T>
     * @return an instance of the single bean matching the required type
     */
    public <T> T getBean(Class<T> clazz) {
        T result;
        Iterator it = apps.iterator();

        while (it.hasNext()) {
            ApplicationContext app = (ApplicationContext) it.next();
            try {
                result = app.getBean(clazz);
                if (result != null) {
                    return result;
                }
            } catch (BeansException ignored) {
            }
        }

        throw new NoSuchBeanDefinitionException(clazz.getName());
    }

    /**
     * 根据bean的名称获取bean
     *
     * @param name the name of the bean to retrieve
     * @return an instance of the bean
     */
    @SuppressWarnings("unchecked")
    public <T> T getBean(String name) {
        T result = null;
        Iterator it = apps.iterator();

        do {
            ApplicationContext app = (ApplicationContext) it.next();
            try {
                result = (T) app.getBean(name);
                if (result != null) {
                    return result;
                }
            } catch (BeansException ignored) {
            }
        } while (!it.hasNext());

        throw new NoSuchBeanDefinitionException(name);
    }

    /**
     * 获取spring上下文中的所有指定类型的bean
     *
     * @param clazz the class or interface to match, or null for all concrete beans
     * @return a Map with the matching beans, containing the bean names as keys and the corresponding bean instances as values
     */
    public <T> Map<String, T> getBeansByType(Class<T> clazz) {
        Iterator it = apps.iterator();
        Map<String, T> results = new HashMap<>(16);
        while (it.hasNext()) {
            ApplicationContext app = (ApplicationContext) it.next();
            results.putAll(app.getBeansOfType(clazz));
        }

        return results;
    }

    public void removeAll() {
        apps.clear();
        apps = null;
    }

    public void addApplicationContext(ApplicationContext context) {
        if (context == null) {
            return;
        }
        apps.add(context);

        if (context.getParent() != null) {
            //递归，将context的所有上一级放入apps中
            this.addApplicationContext(context.getParent());
        }

        if (context instanceof WebApplicationContext) {
            rootPath = ((WebApplicationContext) context).getServletContext().getRealPath("/");
            contextPath = ((WebApplicationContext) context).getServletContext().getContextPath();
        }
    }

    public String getRootPath() {
        if (rootPath != null) {
            return rootPath;
        }
        return "./webapp/";
    }

    public String getContextPath() {
        return contextPath;
    }

    public static ApplicationHelper getInstance() {
        return instance;
    }

}

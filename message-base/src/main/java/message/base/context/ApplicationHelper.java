package message.base.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;

/**
 * applicationContext的辅助类
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 2012-3-8 下午09:56:58
 */
public class ApplicationHelper {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationHelper.class);

    private static ApplicationHelper instance = new ApplicationHelper();

    private String rootPath;

    private String contextPath;

    private Set<ApplicationContext> apps = new HashSet<>();

    //构造器私有，不可在外部进行初始化实例
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
        T result = null;
        Iterator it = apps.iterator();

        while (it.hasNext()) {
            ApplicationContext app = (ApplicationContext) it.next();
            try {
                result = app.getBean(clazz);
                if (result != null)
                    return result;
            } catch (BeansException e) {
                continue;
            }
        }

        if (result == null)
            throw new NoSuchBeanDefinitionException("there is no bean named '" + clazz.getName() + "'");

        return result;
    }

    /**
     * 根据bean的名称获取bean
     *
     * @param name the name of the bean to retrieve
     * @return an instance of the bean
     */
    public Object getBean(String name) {
        Object result = null;
        Iterator it = apps.iterator();

        do {
            ApplicationContext app = (ApplicationContext) it.next();
            try {
                result = app.getBean(name);
                if (result != null)
                    return result;
            } catch (BeansException e) {
                continue;
            }
        } while (!it.hasNext());

        if (result == null)
            throw new NoSuchBeanDefinitionException("there is no bean named '" + name + "'");

        return result;
    }

    /**
     * 获取spring上下文中的所有指定类型的bean
     *
     * @param clazz the class or interface to match, or null for all concrete beans
     * @return a Map with the matching beans, containing the bean names as keys and the corresponding bean instances as values
     */
    public <T> Map<String, T> getBeansByType(Class<T> clazz) {
        Iterator it = apps.iterator();
        Map<String, T> results = new HashMap<>();
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
        if (context == null)
            return;
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
        if (rootPath != null)
            return rootPath;
        return "./webapp/";
    }

    public String getContextPath() {
        return contextPath;
    }

    public static ApplicationHelper getInstance() {
        return instance;
    }

}

package lodsve.webservice;

import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.ClassUtils;
import org.springframework.web.WebApplicationInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.util.Map;

/**
 * Lodsve Webservice Initializer.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version 1.0 2018/1/12 下午10:17
 */
public class LodsveWebserviceInitializer implements WebApplicationInitializer, InitializingBean {
    private static ServletContext servletContext;
    private WebServiceProperties properties;

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        LodsveWebserviceInitializer.servletContext = servletContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (!ClassUtils.isPresent(CXFServlet.class.getName(), this.getClass().getClassLoader())) {
            return;
        }

        CXFServlet servlet = new CXFServlet();
        ServletRegistration.Dynamic dynamic = servletContext.addServlet("CXFServlet", servlet);
        String path = properties.getPath();
        String urlMapping = (path.endsWith("/") ? path + "*" : path + "/*");
        dynamic.addMapping(urlMapping);

        WebServiceProperties.Servlet servletProperties = this.properties.getServlet();
        dynamic.setLoadOnStartup(servletProperties.getLoadOnStartup());
        for (Map.Entry<String, String> entry : servletProperties.getInit().entrySet()) {
            dynamic.setInitParameter(entry.getKey(), entry.getValue());
        }
    }

    public void setProperties(WebServiceProperties properties) {
        this.properties = properties;
    }
}

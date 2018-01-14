package lodsve.webservice;

import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.util.ClassUtils;
import org.springframework.web.WebApplicationInitializer;

import javax.servlet.ServletRegistration.Dynamic;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * Lodsve Webservice Initializer.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version 1.0 2018/1/12 下午10:17
 */
public class LodsveWebserviceInitializer implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        if (!ClassUtils.isPresent(CXFServlet.class.getName(), this.getClass().getClassLoader())) {
            return;
        }

        CXFServlet servlet = new CXFServlet();
        Dynamic dynamic = servletContext.addServlet("CXFServlet", servlet);
        dynamic.setLoadOnStartup(0);
        dynamic.addMapping("/webservice/*");
    }
}

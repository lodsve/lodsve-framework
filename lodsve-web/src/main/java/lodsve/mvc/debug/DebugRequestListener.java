package lodsve.mvc.debug;

import lodsve.core.appllication.ApplicationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.ServletRequestHandledEvent;

/**
 * 系统是否开启debug功能,可以查看请求的详情
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 2012-3-12 上午05:50:23
 */
@Component
public class DebugRequestListener implements ApplicationListener {
    private static final Logger logger = LoggerFactory.getLogger(DebugRequestListener.class);

    @Autowired
    private ApplicationProperties applicationProperties;

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (applicationProperties.isDevMode()) {
            if (!(event instanceof ServletRequestHandledEvent)) {
                logger.debug("the event is not ServletRequestHandledEvent");
                return;
            }

            ServletRequestHandledEvent s = (ServletRequestHandledEvent) event;

            System.out.println("request process info:");
            System.out.println("begin-----------------");
            System.out.println("time=[" + s.getProcessingTimeMillis() + "]");
            System.out.println("url=[" + s.getRequestUrl() + "]");
            System.out.println("client=[" + s.getClientAddress() + "]");
            System.out.println("method=[" + s.getMethod() + "]");
            System.out.println("end-------------------");
        } else {
            logger.debug("the debug switch is false!");
        }

    }

}

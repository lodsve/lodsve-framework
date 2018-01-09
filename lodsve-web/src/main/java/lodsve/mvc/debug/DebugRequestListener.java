package lodsve.mvc.debug;

import lodsve.core.configuration.ApplicationProperties;
import lodsve.mvc.properties.ServerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.ServletRequestHandledEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 系统是否开启debug功能,可以查看请求的详情
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0
 * @createTime 2012-3-12 上午05:50:23
 */
@Component
public class DebugRequestListener implements ApplicationListener {
    private static final Logger logger = LoggerFactory.getLogger(DebugRequestListener.class);
    private static List<Pattern> PATTERNS = new ArrayList<>(16);

    @Autowired
    private ApplicationProperties applicationProperties;
    @Autowired
    private ServerProperties serverProperties;

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (!applicationProperties.isDevMode()) {
            logger.debug("the debug switch is false!");
            return;
        }

        if (!(event instanceof ServletRequestHandledEvent)) {
            logger.debug("the event is not ServletRequestHandledEvent");
            return;
        }

        if (PATTERNS.isEmpty()) {
            initPattern();
        }

        ServletRequestHandledEvent s = (ServletRequestHandledEvent) event;

        String url = s.getRequestUrl();
        String client = s.getClientAddress();
        long time = s.getProcessingTimeMillis();
        String method = s.getMethod();

        if (serverProperties.getDebug().getExcludeAddress().contains(client)) {
            return;
        }
        for (Pattern pattern : PATTERNS) {
            if (pattern.matcher(url).matches()) {
                return;
            }
        }
        if (time > serverProperties.getDebug().getMaxProcessingTime()) {
            if (logger.isWarnEnabled()) {
                logger.warn(String.format("The request '%s' from '%s' with method '%s' execute '%d' more than max time '%d'!Please check it!", url, client, method, time, serverProperties.getDebug().getMaxProcessingTime()));
            }
        }

        System.out.println("request process info:");
        System.out.println("begin-----------------");
        System.out.println("time=[" + time + "]");
        System.out.println("url=[" + url + "]");
        System.out.println("client=[" + client + "]");
        System.out.println("method=[" + method + "]");
        System.out.println("end-------------------");
    }

    private void initPattern() {
        ServerProperties.Debug debug = serverProperties.getDebug();
        List<String> excludeUrl = debug.getExcludeUrl();
        for (String url : excludeUrl) {
            PATTERNS.add(Pattern.compile(url));
        }
    }
}

package lodsve.mvc.debug;

import lodsve.mvc.properties.ServerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
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
public class DebugRequestListener implements ApplicationListener<ServletRequestHandledEvent> {
    private static final Logger logger = LoggerFactory.getLogger(DebugRequestListener.class);
    private static final List<Pattern> PATTERNS = new ArrayList<>(16);
    private static final List<String> DEFAULT_EXCLUDE_URL = new ArrayList<>();

    private boolean debugMode;
    private ServerProperties serverProperties;

    public DebugRequestListener(boolean debugMode, ServerProperties serverProperties) {
        this.debugMode = debugMode;
        this.serverProperties = serverProperties;

        DEFAULT_EXCLUDE_URL.add(".*/v2/api-docs");
        DEFAULT_EXCLUDE_URL.add(".*/swagger-resources");
        DEFAULT_EXCLUDE_URL.add(".*/configuration/ui");
        DEFAULT_EXCLUDE_URL.add(".*/webjars/.*");
        DEFAULT_EXCLUDE_URL.add(".*/swagger-ui.html");
    }

    @Override
    public void onApplicationEvent(ServletRequestHandledEvent event) {
        if (!debugMode) {
            logger.debug("the debug switch is false!");
            return;
        }

        if (PATTERNS.isEmpty()) {
            initPattern();
        }

        String url = event.getRequestUrl();
        String client = event.getClientAddress();
        long time = event.getProcessingTimeMillis();
        String method = event.getMethod();

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
        excludeUrl.addAll(DEFAULT_EXCLUDE_URL);
        for (String url : excludeUrl) {
            PATTERNS.add(Pattern.compile(url));
        }
    }
}

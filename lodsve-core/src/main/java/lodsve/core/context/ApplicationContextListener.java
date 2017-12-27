package lodsve.core.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * 系统初始化时运行此类，将上下文注入到ApplicationHelper中，
 * 运行时，可以从ApplicationHelper中获取bean
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0
 * @createTime 2012-3-8 下午10:16:10
 */
public class ApplicationContextListener implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationContextListener.class);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        logger.info("the applicationContext is " + event.getApplicationContext());

        ApplicationHelper.getInstance().addApplicationContext(event.getApplicationContext());
    }

}

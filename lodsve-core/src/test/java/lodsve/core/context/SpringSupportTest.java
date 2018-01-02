package lodsve.core.context;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2016/12/20 上午10:54
 */
public class SpringSupportTest extends SpringSupport {
    @Autowired
    private DemoService demoService;

    private void say() {
        demoService.say();
    }

    public static void main(String[] args) {
        SpringSupportTest test = new SpringSupportTest();
        test.say();

        DemoService demoService = ApplicationHelper.getInstance().getBean(DemoService.class);
        demoService.say();
    }

    @Override
    public String supportConfigLocation() {
        return "spring/application-context.xml";
    }
}

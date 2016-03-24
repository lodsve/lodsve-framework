package message.swagger.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * swagger资源路径.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/3/23 下午4:17
 */
@Component
public class SpringFoxWebConfigurerAdapter extends WebMvcConfigurerAdapter {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/META-INF/resources/").setCachePeriod(31556926);
    }
}

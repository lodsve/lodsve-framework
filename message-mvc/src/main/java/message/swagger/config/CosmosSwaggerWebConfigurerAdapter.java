package message.swagger.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * swagger资源路径.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/3 下午12:29
 */
@Component
public class CosmosSwaggerWebConfigurerAdapter extends WebMvcConfigurerAdapter {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger/**").addResourceLocations("classpath:/swagger/").setCachePeriod(31556926);
    }
}

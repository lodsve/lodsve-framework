package lodsve.springfox.config;

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
    private static final String SPRING_FOX_UI_MAPPING = "/webjars/springfox-swagger-ui/**";
    private static final String SWAGGER_INDEX = "/swagger-ui.html";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if (!registry.hasMappingForPattern(SPRING_FOX_UI_MAPPING)) {
            registry.addResourceHandler(SPRING_FOX_UI_MAPPING).addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/").setCachePeriod(31556926);
        }
        if (!registry.hasMappingForPattern(SWAGGER_INDEX)) {
            registry.addResourceHandler(SWAGGER_INDEX).addResourceLocations("classpath:/META-INF/resources/swagger-ui.html").setCachePeriod(31556926);
        }
    }
}

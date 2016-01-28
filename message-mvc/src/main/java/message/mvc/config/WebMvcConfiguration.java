package message.mvc.config;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * web mvc 配置,扫描包路径.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/1/28 上午10:58
 */
@Configuration
@ComponentScan(basePackages = {"message.mvc"})
public class WebMvcConfiguration {
    @Autowired
    private ApplicationProperties properties;

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration configuration = new CorsConfiguration();
        if (properties.isDevMode()) {
            configuration.setAllowedOrigins(Lists.newArrayList("*"));
        } else {
            configuration.setAllowedOrigins(Lists.newArrayList(properties.getFrontEndUrl(), properties.getServerUrl()));
        }
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Lists.newArrayList("X-requested-with", "x-auth-token", "Content-Type"));
        configuration.setAllowedMethods(Lists.newArrayList("POST", "GET", "OPTIONS", "DELETE"));
        configuration.setExposedHeaders(Lists.newArrayList("x-auth-token"));
        configuration.setMaxAge(3600l);
        UrlBasedCorsConfigurationSource ccs = new UrlBasedCorsConfigurationSource();
        ccs.registerCorsConfiguration("/**", configuration);
        return new CorsFilter(ccs);
    }
}

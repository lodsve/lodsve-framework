package lodsve.core.config.profile;

import lodsve.core.config.SystemConfig;
import lodsve.core.config.loader.properties.Configuration;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.CollectionUtils;

import java.util.Set;

/**
 * 读取配置的profile.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/1/30 下午3:20
 */
public class ProfileInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment configEnv = applicationContext.getEnvironment();

        Configuration profileConfig = SystemConfig.getFrameworkConfiguration("profiles.properties");
        Set<String> profiles = profileConfig.subset("profiles").getKeys();

        if (CollectionUtils.isEmpty(profiles)) {
            return;
        }

        for (String profile : profiles) {
            if (profileConfig.getBoolean("profiles." + profile, false)) {
                configEnv.addActiveProfile(profile);
            }
        }
    }
}

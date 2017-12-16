package lodsve.core.properties.profile;

import lodsve.core.properties.Env;
import lodsve.core.properties.configuration.Configuration;
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

        Configuration profileConfig = Env.getFrameworkConfiguration("profiles.properties");
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

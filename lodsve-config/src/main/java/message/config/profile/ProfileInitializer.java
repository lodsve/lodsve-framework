package message.config.profile;

import java.util.Set;
import message.config.SystemConfig;
import message.config.loader.properties.Configuration;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.CollectionUtils;

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

        Configuration profileConfig = SystemConfig.subset("profiles");
        Set<String> profiles = profileConfig.getKeys();

        if (CollectionUtils.isEmpty(profiles)) {
            return;
        }

        for (String profile : profiles) {
            if (profileConfig.getBoolean(profile, false)) {
                configEnv.addActiveProfile(profile);
            }
        }
    }
}

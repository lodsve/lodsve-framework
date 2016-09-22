package lodsve.core.config.profile;

import lodsve.core.config.SystemConfig;
import lodsve.core.config.loader.properties.Configuration;
import org.springframework.util.Assert;

/**
 * 读取配置的profile.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2016-9-22 10:37
 */
public class ProfileConfig {
    /**
     * 获取profile的值
     *
     * @param profileName profile name
     * @return true/false
     */
    public static boolean getProfile(String profileName) {
        Assert.hasText(profileName, "profile name is required!");

        Configuration profileConfig = SystemConfig.subset("profiles");
        String result = profileConfig.getString(profileName, Boolean.FALSE.toString());

        return Boolean.TRUE.toString().equals(result);
    }
}

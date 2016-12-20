package lodsve.core.context;

/**
 * 所有main方法继承此类.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2016/12/20 上午10:17
 */
public class SpringSupport {
    SpringSupport() {
        new SpringContext.Builder().config(supportConfigLocation()).bean(this).build();
    }

    /**
     * 重写此方法，提供spring配置文件的路径，否则使用默认的配置文件
     *
     * @return spring配置文件的路径
     */
    public String supportConfigLocation() {
        return "";
    }
}

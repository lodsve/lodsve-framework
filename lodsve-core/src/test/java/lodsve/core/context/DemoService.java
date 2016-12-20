package lodsve.core.context;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2016/12/20 上午10:55
 */
public class DemoService {
    private String sayWhat;

    void say() {
        System.out.println(sayWhat);
    }

    public void setSayWhat(String sayWhat) {
        this.sayWhat = sayWhat;
    }
}

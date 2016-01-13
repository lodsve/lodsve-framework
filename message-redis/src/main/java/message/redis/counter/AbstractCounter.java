package message.redis.counter;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/9/28 下午1:05
 */
public abstract class AbstractCounter implements Counter {
    @Override
    public void increment(String catalog, String key, int step) {
        int times = get(catalog, key);
        if(times == -1) {
            // 以前没有值
            set(catalog, key, 0);
        }

        _increment(catalog, key, step);
    }

    public abstract void _increment(String catalog, String key, int step);
}

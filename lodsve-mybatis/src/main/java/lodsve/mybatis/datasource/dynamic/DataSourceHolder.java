package lodsve.mybatis.datasource.dynamic;

/**
 * 多数据源保存选择的数据源.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2017/12/14 下午6:03
 */
public class DataSourceHolder {
    private static final ThreadLocal<String> DATAS_SOURCE = new ThreadLocal<>();

    public static String get() {
        return DATAS_SOURCE.get();
    }

    static void set(String dataSource) {
        DATAS_SOURCE.set(dataSource);
    }

    static void clear() {
        DATAS_SOURCE.remove();
    }
}

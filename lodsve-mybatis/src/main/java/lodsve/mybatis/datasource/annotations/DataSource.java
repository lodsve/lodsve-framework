package lodsve.mybatis.datasource.annotations;

/**
 * 数据源.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2017/12/14 下午4:40
 */
public @interface DataSource {
    /**
     * 数据源名称
     *
     * @return
     */
    String value();

    /**
     * 是否是默认数据源,如果多个是默认数据源则选择最后一个作为默认数据源
     *
     * @return
     */
    boolean isDefault() default false;
}

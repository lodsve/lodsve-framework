package lodsve.base.utils;

/**
 * 对sql/hql语句的处理类
 *
 * @author sunhao(sunhao.java@gmail.com)
 */
public class SqlUtils {

    /**
     * 私有化构造器
     */
    private SqlUtils(){}

    /**
     * 获得query.setFirstResult(start)中的start值
     *
     * @param page
     * @param num
     * @return
     */
    public static int getStartNum(int page, int num) {
        if (page < 1) {
            page = 1;
        }
        page--;

        return page * num;
    }

    /**
     * 组装sql中like后的字符串(%...%)
     *
     * @param likeString
     * @return
     */
    public static String makeLikeString(String likeString) {
        if (StringUtils.isNotEmpty(StringUtils.trim(likeString))) {
            likeString = "%" + StringUtils.trim(likeString) + "%";
        }
        return likeString;
    }

	/**
	 * 生成纯正SQL语句的查询语句
	 * 
	 * @param sql
	 * @return
	 */
    public static String getCountSql(String sql) {
        StringBuffer sb = new StringBuffer();
        if (StringUtils.isNotEmpty(sql)) {
            sb.append("select count(*) from (").append(sql).append(")");
            return sb.toString();
        } else {
            return StringUtils.EMPTY;
        }
    }
}

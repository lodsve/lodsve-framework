package lodsve.mybatis.enums;

import org.springframework.util.Assert;

/**
 * 表示数据库类型的枚举.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/6/1 下午4:18
 */
public enum DbType {
    /**
     * 数据库类型
     */
    DB_ORACLE("O", "Oracle"), DB_MYSQL("M", "MySQL");

    /**
     * DbType
     *
     * @param dbType String
     * @param name   String
     */
    DbType(String dbType, String name) {
        this.dbType = dbType;
        this.name = name;
    }

    /**
     * dbType
     */
    private String dbType;

    /**
     * name
     */
    private String name;

    /**
     * Description: <br>
     *
     * @return String
     */
    public String getDbType() {
        return dbType;
    }

    /**
     * Description: <br>
     *
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * Description: <br>
     *
     * @param eval String
     * @return <br>
     */
    public static DbType eval(String eval) {
        Assert.hasLength(eval, "eval is required!");

        for (DbType dt : DbType.values()) {
            if (eval.equals(dt.getDbType())) {
                return dt;
            }
        }

        return null;
    }
}

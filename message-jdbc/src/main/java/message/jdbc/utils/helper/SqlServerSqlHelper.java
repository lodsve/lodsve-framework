package message.jdbc.utils.helper;

import message.base.utils.ApplicationHelper;
import message.jdbc.key.impl.generic.SqlServerMaxValueIncrementer;
import message.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.DatabaseMetaData;

/**
 * SQL Server 辅助类.
 * 
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 2012-8-3 下午10:05:34
 */
public class SqlServerSqlHelper extends SqlHelper {
    private static final Logger logger = LoggerFactory.getLogger(SqlServerSqlHelper.class);

	public String getPageSql(String sql, int start, int num) {
		//TODO 实现sql server的分页查询语句
		return null;
	}

    public Object getNextId(String sequenceName) {
        if(StringUtils.isEmpty(sequenceName)){
            logger.warn("given null sequenceName!");
            return null;
        }

        SqlServerMaxValueIncrementer sqlServerMaxValueIncrementer = null;
        if(super.idGenerator != null && super.idGenerator instanceof SqlServerMaxValueIncrementer)
            sqlServerMaxValueIncrementer = (SqlServerMaxValueIncrementer) super.idGenerator;
        else
            sqlServerMaxValueIncrementer = ApplicationHelper.getInstance().getBean(SqlServerMaxValueIncrementer.class);

        if(sqlServerMaxValueIncrementer == null){
            logger.error("can not get any SqlServerMaxValueIncrementer!");
            return null;
        }

        String nextId = sqlServerMaxValueIncrementer.nextStringValue(sequenceName);

        return nextId;
    }

    public String existTableSQL(String tableName, DataSource dataSource) throws Exception {
        //todo 可能有错误
        DatabaseMetaData metaData = dataSource.getConnection().getMetaData();
        String name = metaData.getUserName();
        String databaseName = dataSource.getConnection().getCatalog();

        String sql = "select count(*) from sysobjects where id = object_id('" + databaseName + "." + name + "."
                + tableName + "')";

        return sql;
    }

}

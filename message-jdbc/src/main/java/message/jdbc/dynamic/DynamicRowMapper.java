package message.jdbc.dynamic;

import message.jdbc.utils.helper.SqlHelper;
import org.springframework.jdbc.core.RowMapper;

/**
 * 动态行映射
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2012-4-10 上午02:19:20
 */
public interface DynamicRowMapper extends RowMapper {

    public SqlHelper getSqlHelper();

    public void setSqlHelper(SqlHelper sh);
}

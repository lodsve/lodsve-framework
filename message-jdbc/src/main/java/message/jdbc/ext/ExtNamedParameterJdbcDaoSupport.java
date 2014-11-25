package message.jdbc.ext;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 * 扩展的NamedParameterJdbcDaoSupport
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2012-4-10 上午12:24:57
 */
public class ExtNamedParameterJdbcDaoSupport extends NamedParameterJdbcDaoSupport {
	private ExtNamedParameterJdbcTemplate extNamedParameterJdbcTemplate;

	protected void initTemplateConfig() {
		this.extNamedParameterJdbcTemplate = new ExtNamedParameterJdbcTemplate(getJdbcTemplate());
	}

	public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
		return this.extNamedParameterJdbcTemplate;
	}

	public ExtNamedParameterJdbcTemplate getExtNamedParameterJdbcTemplate() {
		return extNamedParameterJdbcTemplate;
	}

}

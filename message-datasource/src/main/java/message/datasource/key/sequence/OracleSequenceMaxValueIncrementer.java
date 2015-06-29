package message.datasource.key.sequence;


/**
 * Oracle数据库主键自增长策略
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2012-4-11 上午08:57:30
 */
public class OracleSequenceMaxValueIncrementer extends AbstractSequenceMaxValueIncrementer {

	public String getQuerySequence(String name) {
		return "select " + name + ".nextval from dual";
	}

}

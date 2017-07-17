package lodsve.mybatis.plugins.monitor;

import lodsve.mybatis.exception.MyBatisException;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Proxy;
import java.sql.Statement;
import java.util.Properties;

/**
 * mybatis性能监控.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2017/6/12 下午8:23
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class}),
        @Signature(type = StatementHandler.class, method = "update", args = {Statement.class}),
        @Signature(type = StatementHandler.class, method = "batch", args = {Statement.class})})
public class MonitorInterceptor implements Interceptor {
    private static final Logger logger = LoggerFactory.getLogger(MonitorInterceptor.class);
    /**
     * SQL 执行最大时长，超过自动停止运行，有助于发现问题。
     */
    private long maxTime = 0;

    /**
     * SQL 是否格式化
     */
    private boolean format = false;

    /**
     * 是否写入日志文件<br>
     * true 写入日志文件，不阻断程序执行！<br>
     * 超过设定的最大执行时长异常提示！
     */
    private boolean writeInLog = false;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Statement statement;
        Object firstArg = invocation.getArgs()[0];
        if (Proxy.isProxyClass(firstArg.getClass())) {
            statement = (Statement) SystemMetaObject.forObject(firstArg).getValue("h.statement");
        } else {
            statement = (Statement) firstArg;
        }
        try {
            statement.getClass().getDeclaredField("stmt");
            statement = (Statement) SystemMetaObject.forObject(statement).getValue("stmt.statement");
        } catch (Exception e) {
            // do nothing
        }

        // 获取执行 SQL
        String originalSql = statement.toString();
        int index = originalSql.indexOf(':');
        if (index > 0) {
            originalSql = originalSql.substring(index + 1, originalSql.length());
        }

        // 计算执行 SQL 耗时
        long start = System.currentTimeMillis();
        Object result = invocation.proceed();
        long timing = System.currentTimeMillis() - start;

        // 格式化 SQL 打印执行结果
        Object target = realTarget(invocation.getTarget());
        MetaObject metaObject = SystemMetaObject.forObject(target);
        MappedStatement ms = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        StringBuilder formatSql = new StringBuilder();
        formatSql.append(" Time：").append(timing);
        formatSql.append(" ms - ID：").append(ms.getId());
        formatSql.append("\n Execute SQL：").append(originalSql).append("\n");
        if (writeInLog) {
            if (maxTime >= 1 && timing > maxTime) {
                logger.error(formatSql.toString());
            } else {
                logger.debug(formatSql.toString());
            }
        } else {
            System.err.println(formatSql.toString());
            if (maxTime >= 1 && timing > maxTime) {
                throw new MyBatisException(" The SQL execution time is too large, please optimize ! ");
            }
        }
        return result;
    }

    /**
     * 获得真正的处理对象,可能多层代理.
     *
     * @param target 处理对象
     * @return 真正的处理对象
     */
    private Object realTarget(Object target) {
        if (Proxy.isProxyClass(target.getClass())) {
            MetaObject metaObject = SystemMetaObject.forObject(target);
            return realTarget(metaObject.getValue("h.target"));
        }
        return target;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties prop) {

    }

    public static Logger getLogger() {
        return logger;
    }

    public long getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(long maxTime) {
        this.maxTime = maxTime;
    }

    public boolean isFormat() {
        return format;
    }

    public void setFormat(boolean format) {
        this.format = format;
    }

    public boolean isWriteInLog() {
        return writeInLog;
    }

    public void setWriteInLog(boolean writeInLog) {
        this.writeInLog = writeInLog;
    }
}

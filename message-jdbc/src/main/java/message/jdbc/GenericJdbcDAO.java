package message.jdbc;

import message.base.Constants;
import message.base.pagination.PaginationSupport;
import message.base.pagination.PaginationUtils;
import message.cache.Cache;
import message.cache.CacheManager;
import message.jdbc.bean.BeanPersistenceDef;
import message.jdbc.bean.BeanPersistenceHelper;
import message.jdbc.dynamic.ColumnMapRowMapper;
import message.jdbc.dynamic.DynamicBeanRowMapper;
import message.jdbc.ext.ExtBeanPropertySqlParameterSource;
import message.jdbc.ext.ExtMapSqlParameterSource;
import message.jdbc.ext.ExtNamedParameterJdbcDaoSupport;
import message.jdbc.key.IDGenerator;
import message.jdbc.utils.helper.SqlHelper;
import message.utils.ObjectUtils;
import message.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * query for more types
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2012-4-10 上午12:32:41
 */
public class GenericJdbcDAO extends ExtNamedParameterJdbcDaoSupport {
	private static final Logger logger = LoggerFactory.getLogger(GenericJdbcDAO.class);

    private RowMapper rowMapper;
    private SqlHelper sqlHelper;
    private IDGenerator idGenerator;
    private CacheManager cacheManager;

    private static final String DEFAULT_PRIMARY_KEY = "pk_id";
    private static final String DEFAULT_DELETE_FLAG = "delete_flag";

    /**
     * 查询得到int型
     *
     * @param sql		query sql
     * @param params	need parameter
     * @return			result(int)
     * @throws org.springframework.dao.DataAccessException
     */
    public int queryForInt(String sql, Map params) throws DataAccessException {
        return this.getNamedParameterJdbcTemplate().queryForInt(sql, params);
    }

    /**
     * 查询得到long型
     *
     * @param sql		query sql
     * @param params	need parameter
     * @return			result(long)
     * @throws org.springframework.dao.DataAccessException
     */
    public long queryForLong(String sql, Map params) throws DataAccessException {
        return this.getNamedParameterJdbcTemplate().queryForLong(sql, params);
    }

    /**
     * query for map
     *
     * @param sql		query sql
     * @param params	need parameter
     * @return			result(map)
     * @throws org.springframework.dao.DataAccessException
     */
    public Map queryForMapList(String sql, Map params) throws DataAccessException {
        return (Map) this.queryForObject(sql, params, getRowMapper());
    }

    /**
     * query for list
     *
     * @param sql
     * @param params
     * @return
     * @throws org.springframework.dao.DataAccessException
     */
    public List queryForList(String sql, Map params) throws DataAccessException {
        return this.queryForList(sql, params, getRowMapper());
    }

    /**
     * update by sql with given parameters
     *
     * @param sql
     * @param params
     * @return
     * @throws org.springframework.dao.DataAccessException
     */
    public int update(String sql, Object[] params) throws DataAccessException {
        return this.getJdbcTemplate().update(sql, params);
    }

    /**
     * update by sql
     *
     * @param sql
     * @return
     * @throws org.springframework.dao.DataAccessException
     */
    public int update(String sql) throws DataAccessException {
        return this.getJdbcTemplate().update(sql);
    }

    /**
     * update by sql with preparedStatementSetter
     *
     * @param sql
     * @return
     * @throws org.springframework.dao.DataAccessException
     */
    public int update(String sql, PreparedStatementSetter setter) throws DataAccessException {
        return this.getJdbcTemplate().update(sql, setter);
    }

    /**
     * query by sql, given params type is <code>java.util.Map</code><br/>
     *
     * @param sql
     * @param params
     * @return
     * @throws org.springframework.dao.DataAccessException
     */
    public int update(String sql, Map params) throws DataAccessException {
        ExtMapSqlParameterSource parameterSource = new ExtMapSqlParameterSource(params, this.sqlHelper);
        return this.getNamedParameterJdbcTemplate().update(sql, parameterSource);
    }

    /**
     * update by sql when given SqlParameterSource
     *
     * @param sql
     * @param paramSource
     * @return
     * @throws org.springframework.dao.DataAccessException
     */
    public int update(String sql, SqlParameterSource paramSource) throws DataAccessException {
        return this.getNamedParameterJdbcTemplate().update(sql, paramSource);
    }

    /**
     * update sql when given parameter in an object
     * TODO need to study ExtBeanPropertySqlParameterSource
     *
     * @param sql
     * @param obj
     * @return
     * @throws org.springframework.dao.DataAccessException
     */
    public int updateByBean(String sql, Object obj) throws DataAccessException {
        return this.getNamedParameterJdbcTemplate().update(sql, new ExtBeanPropertySqlParameterSource(obj, this.sqlHelper));
    }

    /**
     * TODO the same as top method
     *
     * @param sql
     * @param objs
     * @return
     * @throws org.springframework.dao.DataAccessException
     */
    public int[] updateBatchByBean(final String sql, final List objs) throws DataAccessException {
        return null;
    }

    /**
     * query for list
     *
     * @param sql		query sql
     * @param params	need parameter
     * @param rowMapper
     * @return
     * @throws org.springframework.dao.DataAccessException
     */
    public List queryForList(String sql, Map params, RowMapper rowMapper) throws DataAccessException {
        return this.getNamedParameterJdbcTemplate().query(sql, params, rowMapper);
    }

    /**
     * query for object
     *
     * @param sql
     * @param params
     * @param rowMapper
     * @return
     * @throws org.springframework.dao.DataAccessException
     */
    public Object queryForObject(String sql, Map params, RowMapper rowMapper) {
        try{
            return this.getNamedParameterJdbcTemplate().queryForObject(sql, params, rowMapper);
        } catch (EmptyResultDataAccessException e){
            return null;
        }
    }
    
    /**
     * 按给定参数objs查询返回一个class对象
     * 
     * @param <T>
     * @param sql			sql
     * @param mapper		RowMapper
     * @param objs			参数
     * @return
     */
	public <T> T queryForObject(String sql, RowMapper<T> mapper, Object[] objs) {
		try {
			return getJdbcTemplate().queryForObject(sql, mapper, objs);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

    /**
     * query, return will be make as the class bean what you given
     *
     * @param sql
     * @param params
     * @param clazz
     * @return
     * @throws org.springframework.dao.DataAccessException
     */
    public <T> T queryForBean(String sql, Map params, Class<T> clazz) throws DataAccessException {
        return (T) this.queryForObject(sql, params, DynamicBeanRowMapper.getInstance(clazz, this.getSqlHelper(), sql));
    }
    
    /**
     * 按给定参数objs查询返回一个class对象
     * 
     * @param <T>
     * @param sql			sql
     * @param clazz			class
     * @param objs			对象
     * @return
     */
    public <T> T queryForBean(String sql, Class<T> clazz, Object[] objs){
    	return (T) this.queryForObject(sql, DynamicBeanRowMapper.getInstance(clazz, getSqlHelper(), sql), objs);
    }

    /**
     * query for a list, begin index is start, offset is num
     *
     * @param sql			query sql
     * @param start			begin index
     * @param num			offset
     * @param params
     * @param rowMapper
     * @return
     * @throws org.springframework.dao.DataAccessException
     */
    public List queryForObjectList(String sql, int start, int num, Map params, RowMapper rowMapper) throws DataAccessException {
        sql = this.getSqlHelper().getPageSql(sql, start, num);
        return this.queryForList(sql, params, rowMapper);
    }

    /**
     * query for a bean list as given class begin index is start, offset is num
     *
     * @param sql			query sql
     * @param start			begin index
     * @param num			offset
     * @param params
     * @param clazz			bean class
     * @return
     * @throws org.springframework.dao.DataAccessException
     */
    public <T> List<T> queryForBeanList(String sql, int start, int num, Map params, Class<T> clazz) throws DataAccessException {
        String pageSql = this.getSqlHelper().getPageSql(sql, start, num);

        return this.queryForList(pageSql, params, DynamicBeanRowMapper.getInstance(clazz, this.getSqlHelper(), sql));
    }

    /**
     * query for pagination
     *
     * @param sql			query sql, must be given
     * @param countSql		query count sql, maybe null
     * @param start			begin index
     * @param num			offset
     * @param params
     * @param clazz
     * @return
     * @throws org.springframework.dao.DataAccessException
     */
    public <T> PaginationSupport<T> getBeanPaginationSupport(String sql, String countSql, int start, int num, Map params, Class<T> clazz)
            throws DataAccessException {
        countSql = countSql == null ? this.getSqlHelper().getCountSql(sql) : countSql;
        int count = this.queryForInt(countSql, params);

        List<T> result = null;
        if(count == 0)
            result = new ArrayList<T>();
        else
            result = this.queryForBeanList(sql, start, num, params, clazz);

        PaginationSupport<T> ps = PaginationUtils.makePagination(result, count, num, start);
        return ps;
    }

    /**
     * query for object pagination
     *
     * @param sql			query sql, must be given
     * @param countSql		query count sql, maybe null
     * @param start			begin index
     * @param num			offset
     * @param params
     * @param rowMapper
     * @return
     * @throws org.springframework.dao.DataAccessException
     */
    public PaginationSupport getObjectPaginationSupport(String sql, String countSql, int start, int num, Map params, RowMapper rowMapper)
            throws DataAccessException {
        countSql = countSql == null ? this.getSqlHelper().getCountSql(sql) : countSql;
        int count = this.queryForInt(countSql, params);

        List result = null;
        if(count == 0)
            result = Collections.EMPTY_LIST;
        else
            result = this.queryForObjectList(sql, start, num, params, rowMapper);

        PaginationSupport ps = PaginationUtils.makePagination(result, count, num, start);
        return ps;
    }

    /**
     * get next long pkId from database by given sequence
     *
     * @param name	sequence name
     * @return
     * @throws org.springframework.dao.DataAccessException
     */
    public long generateLongId(String name) throws DataAccessException {
        return this.idGenerator.nextLongValue(name);
    }

    /**
     * get next int pkId from database by given sequence
     *
     * @param name	sequence name
     * @return
     * @throws org.springframework.dao.DataAccessException
     */
    public int generateIntId(String name) throws DataAccessException {
        return this.idGenerator.nextIntValue(name);
    }

    /**
     * get next string pkId from database by given sequence
     *
     * @param name	sequence name
     * @return
     * @throws org.springframework.dao.DataAccessException
     */
    public String generateStringId(String name) throws DataAccessException {
        return this.idGenerator.nextStringValue(name);
    }

    /**
     * update
     *
     * @param tableName        table name
     * @param columnParams		colum
     * @param whereParams		where
     * @return					update column rows num
     * @throws org.springframework.dao.DataAccessException
     */
    public int commUpdate(String tableName, Map columnParams, Map whereParams) throws DataAccessException {
        if(StringUtils.isEmpty(tableName) || columnParams == null || columnParams.size() < 1) {
            return 0;
        }

        StringBuffer sql = new StringBuffer();
        sql.append("update ").append(tableName).append(" t ").append(" set ");

        Iterator<String> it1 = columnParams.keySet().iterator();
        while(it1.hasNext()) {
            String key = it1.next();
            Object value = columnParams.get(key);

            if(value instanceof String) {
                sql.append(" t.").append(key).append(" = ").append("'").append(value.toString()).append("', ");
            } else {
                sql.append(" t.").append(key).append(" = ").append(value.toString()).append(", ");
            }
        }

        if(sql.lastIndexOf(",") != -1){
            sql = StringUtils.substringbuffer(sql, 0, sql.length() - 2);
        }

        if(whereParams != null && whereParams.size() > 0){
            sql.append(" where 1 = 1 ");
            Iterator<String> it2 = whereParams.keySet().iterator();
            while(it2.hasNext()){
                String key = it2.next();
                Object value = whereParams.get(key);

                if(value instanceof String) {
                    sql.append(" and t.").append(key).append(" = ").append("'").append(value.toString()).append("' ");
                } else {
                    sql.append(" and t.").append(key).append(" = ").append(value.toString());
                }
            }
        }

        return this.update(sql.toString());
    }

    /**
     * update
     *
     * @param tableName
     * @param columnParams
     * @return
     * @throws org.springframework.dao.DataAccessException
     */
    public int commUpdate(String tableName, Map columnParams) throws DataAccessException {
        return this.commUpdate(tableName, columnParams, null);
    }

    /**
     * 彻底删除(默认主键为pk_id)
     * 
     * @param tableName     表名
     * @param pkId          主键值
     * @throws Exception
     */
    public void commDelete(String tableName, Long pkId) throws Exception {
        this.commDelete(tableName, DEFAULT_PRIMARY_KEY, pkId);
    }

    /**
     * 彻底删除
     *
     * @param tableName     表名
     * @param keyColumn     主键名
     * @param pkId          主键值
     * @throws Exception
     */
    public int commDelete(String tableName, String keyColumn, Long pkId) throws Exception {
        if(StringUtils.isEmpty(tableName) || StringUtils.isEmpty(keyColumn) || pkId == null){
            logger.error("all params is required!");
            return 0;
        }
        StringBuffer sql = new StringBuffer("delete from ");
        sql.append(tableName).append(" where ").append(keyColumn).append(" = :pkId ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("pkId", pkId);

        return this.update(sql.toString(), params);
    }

    /**
     * 安全软删除
     * 
     * @param tableName         表名
     * @param keyColumn         主键名
     * @param pkId              主键值
     * @param deleteFlag        是否删除的标识位字段名
     * @return
     * @throws Exception
     */
    public int commSafeDelete(String tableName, String keyColumn, Long pkId, String deleteFlag) throws Exception {
        if(StringUtils.isEmpty(tableName) || StringUtils.isEmpty(keyColumn) || pkId == null || StringUtils.isEmpty(deleteFlag)){
            logger.error("all params is required!");
            return 0;
        }
        Map<String, Object> columnParams = new HashMap<String, Object>();
        Map<String, Object> whereParams = new HashMap<String, Object>();
        columnParams.put(deleteFlag, Constants.DELETE_YES);
        whereParams.put(keyColumn, pkId);

        return this.commUpdate(tableName, columnParams, whereParams);
    }

    /**
     * 安全软删除(主键名默认为pk_id)
     *
     * @param tableName         表名
     * @param pkId              主键值
     * @param deleteFlag        是否删除的标识位字段名
     * @return
     * @throws Exception
     */
    public int commSafeDelete(String tableName, Long pkId, String deleteFlag) throws Exception {
        return this.commSafeDelete(tableName, DEFAULT_PRIMARY_KEY, pkId, deleteFlag);
    }

    /**
     * 安全软删除(是否删除的标识位字段名默认为delete_flag)
     *
     * @param tableName         表名
     * @param keyColumn         主键名
     * @param pkId              主键值
     * @return
     * @throws Exception
     */
    public int commSafeDelete(String tableName, String keyColumn, Long pkId) throws Exception {
        return this.commSafeDelete(tableName, keyColumn, pkId, DEFAULT_DELETE_FLAG);
    }

    /**
     * 安全软删除(主键名默认为pk_id,是否删除的标识位字段名默认为delete_flag)
     *
     * @param tableName         表名
     * @param pkId              主键值
     * @return
     * @throws Exception
     */
    public int commSafeDelete(String tableName, Long pkId) throws Exception {
        return this.commSafeDelete(tableName, pkId, DEFAULT_DELETE_FLAG);
    }
    
    /**
     * 直接插入一个对象
     * 
     * @param entity			要插入的对象
     * @return
     * @throws Exception 
     */
    public <T> T genericInsert(T entity) throws Exception{
    	if(ObjectUtils.isEmpty(entity)){
    		logger.debug("given entity is null!return null!");
    		return null;
    	}
    	BeanPersistenceDef beanPersistenceDef = BeanPersistenceHelper.getBeanPersistenceDef(entity.getClass());
    	String insertSql = beanPersistenceDef.getInsertSql();
    	Object id = null;
    	if(StringUtils.isNotEmpty(beanPersistenceDef.getIdFieldName())){
    		BeanWrapperImpl bw = new BeanWrapperImpl(entity);
    		id = bw.getPropertyValue(beanPersistenceDef.getIdFieldName());
    		if(id == null){
    			logger.debug("id is null, build it!");
                id = this.idGenerator.nextObjectValue(beanPersistenceDef.getIdClass(), beanPersistenceDef.getGenerator());
    			bw.setPropertyValue(beanPersistenceDef.getIdFieldName(), id);
    		}
    	}
    	logger.debug("insert sql is '{}' for entity '{}'", insertSql, entity);
    	
    	updateByBean(insertSql, entity);
        insertObjectIntoCache(entity, beanPersistenceDef);

    	return entity;
    }
    
    /**
     * 获取一个对象根据主键
     * 
     * @param <T>				对象类型
     * @param clazz				对象的class
     * @param key				主键
     * @return
     * @throws Exception
     */
    public <T> T genericLoad(Class<T> clazz, Serializable key) throws Exception {
    	if(clazz == null || key == null){
    		logger.debug("given clazz or key is null!return null!");
    		return null;
    	}
    	BeanPersistenceDef beanPersistenceDef = BeanPersistenceHelper.getBeanPersistenceDef(clazz);
    	if(beanPersistenceDef == null || StringUtils.isEmpty(beanPersistenceDef.getIdFieldName())){
    		logger.debug("bean is not exist or bean id is not found!");
    		return null;
    	}

        T object = this.loadObjectFromCache(key, beanPersistenceDef);
        if(object == null){
            String queryOneSql = beanPersistenceDef.getSelectOneSql();
            if(logger.isDebugEnabled())
                logger.debug("get query sql is '{}' for bean '{}' what key is '{}'", new Object[]{queryOneSql, clazz, key});
            object = this.queryForBean(queryOneSql, clazz, new Object[]{key});

            //加入缓存中
            this.insertObjectIntoCache(object, beanPersistenceDef);
        }

    	return object;
    }
    
    /**
     * 直接更新一个对象
     * 
     * @param obj
     * @throws Exception
     */
    public void genericUpdate(Object obj) throws Exception {
    	if(ObjectUtils.isEmpty(obj)){
    		logger.debug("given object is null!");
    		return;
    	}
    	BeanPersistenceDef beanPersistenceDef = BeanPersistenceHelper.getBeanPersistenceDef(obj.getClass());
    	String updateSql = beanPersistenceDef.getUpdateSql();
    	if(logger.isDebugEnabled())
    		logger.debug("get update sql for bean '{}' is '{}'", new Object[]{obj.getClass(), updateSql});
    	
    	updateByBean(updateSql, obj);

        //1.先删除缓存中的对象
        this.removeObjectFromCache(obj, beanPersistenceDef);
        //2.向缓存中添加这个新的对象
        this.insertObjectIntoCache(obj, beanPersistenceDef);
    }
    
    /**
     * 删除一个对象
     * 
     * @param obj
     * @throws Exception
     */
    public void genericDelete(Object obj) throws Exception {
    	if(ObjectUtils.isEmpty(obj)){
    		logger.debug("given object is null!");
    		return;
    	}
    	BeanPersistenceDef beanPersistenceDef = BeanPersistenceHelper.getBeanPersistenceDef(obj.getClass());
    	String deleteSql = beanPersistenceDef.getDeleteSql();
    	if(logger.isDebugEnabled())
    		logger.debug("get delete sql for bean '{}' is '{}'", new Object[]{obj.getClass(), deleteSql});
    	updateByBean(deleteSql, obj);
        //1.删除缓存中的对象
        this.removeObjectFromCache(obj, beanPersistenceDef);
    }
    
    /**
     * 根据主键删除一个对象
     * 
     * @param clazz
     * @param key
     * @throws Exception
     */
    public void genericDelete(Class<?> clazz, Serializable key) throws Exception {
    	if(clazz == null || key == null){
    		logger.debug("given clazz or key is null!return null!");
    		return;
    	}
    	BeanPersistenceDef beanPersistenceDef = BeanPersistenceHelper.getBeanPersistenceDef(clazz);
    	if(beanPersistenceDef == null || StringUtils.isEmpty(beanPersistenceDef.getIdFieldName())){
    		logger.debug("bean is not exist or bean id is not found!");
    		return;
    	}
    	
    	String deleteSql = beanPersistenceDef.getDeleteSql();
        String idKey = ":" + beanPersistenceDef.getIdFieldName();
        deleteSql = StringUtils.replace(deleteSql, idKey, "?");
    	if(logger.isDebugEnabled())
    		logger.debug("get query sql is '{}' for bean '{}' what key is '{}'", new Object[]{deleteSql, clazz, key});
    	update(deleteSql, new Object[]{key});
        //1.删除缓存中的对象
        this.removeObjectFromCache(key, beanPersistenceDef);
    }


    /**
     * 从缓存中取对象
     *
     * @param key                       主键
     * @param beanPersistenceDef        bean自定义对象
     * @param <T>                       返回类型
     * @return
     */
    private <T> T loadObjectFromCache(Serializable key, BeanPersistenceDef beanPersistenceDef){
        if(!canCache(beanPersistenceDef) || key == null){
            return null;
        }

        Cache cache = getCacheRegion(beanPersistenceDef);
        String cacheKey = this.getCacheKey(key, null, beanPersistenceDef);
        if(StringUtils.isEmpty(cacheKey)){
            logger.debug("no cache!");
            return null;
        }

        return (T) cache.get(cacheKey);
    }

    /**
     * 放入缓存
     *
     * @param object                    需要放入缓存的对象
     * @param beanPersistenceDef        bean自定义对象
     */
    private void insertObjectIntoCache(Object object, BeanPersistenceDef beanPersistenceDef){
        if(!canCache(beanPersistenceDef)){
            return;
        }

        Cache cache = this.getCacheRegion(beanPersistenceDef);
        String cacheKey = this.getCacheKey(null, object, beanPersistenceDef);

        cache.put(cacheKey, object);
    }

    /**
     * 从缓存中移除对象
     *
     * @param object                    需要从缓存中移除的对象
     * @param beanPersistenceDef        bean自定义对象
     */
    private void removeObjectFromCache(Object object, BeanPersistenceDef beanPersistenceDef){
        if(!canCache(beanPersistenceDef)){
            return;
        }

        Cache cache = this.getCacheRegion(beanPersistenceDef);
        String cacheKey = this.getCacheKey(null, object, beanPersistenceDef);
        cache.remove(cacheKey);
    }

    /**
     * 根据主键从缓存中移除对象
     *
     * @param key                       主键
     * @param beanPersistenceDef        bean自定义对象
     */
    private void removeObjectFromCache(Serializable key, BeanPersistenceDef beanPersistenceDef){
        if(!canCache(beanPersistenceDef)){
            return;
        }

        Cache cache = this.getCacheRegion(beanPersistenceDef);
        String cacheKey = this.getCacheKey(key, null, beanPersistenceDef);
        cache.remove(cacheKey);
    }

    /**
     * 获取对象在缓存中的key
     *
     * @param key                       主键值
     * @param object                    需要放入缓存的对象
     * @param beanPersistenceDef        bean自定义对象
     * @return
     */
    private String getCacheKey(Serializable key, Object object, BeanPersistenceDef beanPersistenceDef){
        if(!canCache(beanPersistenceDef)){
            return StringUtils.EMPTY;
        }

        StringBuffer sb = new StringBuffer();
        sb.append(beanPersistenceDef.getBeanClazz().getName().toString());
        sb.append("#");

        if(key != null){
            sb.append(key);
        } else if(key == null && object != null){
            BeanWrapperImpl beanWrapper = new BeanWrapperImpl(object);
            sb.append(beanWrapper.getPropertyValue(beanPersistenceDef.getIdFieldName()));
        } else {
            return StringUtils.EMPTY;
        }

        return sb.toString();
    }

    /**
     * 获取缓存域
     *
     * @param beanPersistenceDef        bean自定义对象
     * @return
     */
    private Cache getCacheRegion(BeanPersistenceDef beanPersistenceDef){
        if(!canCache(beanPersistenceDef)){
            return null;
        }

        Cache cache = this.cacheManager.getCache(beanPersistenceDef.getCacheRegion());
        return cache;
    }

    /**
     * 判断一个对象是否需要缓存
     *
     * @param beanPersistenceDef        bean自定义对象
     * @return
     */
    private boolean canCache(BeanPersistenceDef beanPersistenceDef){
        boolean need = this.cacheManager != null && beanPersistenceDef != null && beanPersistenceDef.isCacheEnable();
        if(!need){
            logger.debug("no cache!");
        }

        return need;
    }

    protected void initDao() throws Exception {
        super.initDao();

        ColumnMapRowMapper rm = new ColumnMapRowMapper();
        rm.setSqlHelper(sqlHelper);

        this.setRowMapper(rm);
    }

    public RowMapper getRowMapper() {
        return rowMapper;
    }

    public void setRowMapper(RowMapper rowMapper) {
        this.rowMapper = rowMapper;
    }

    public SqlHelper getSqlHelper() {
        return sqlHelper;
    }

    public void setSqlHelper(SqlHelper sqlHelper) {
        this.sqlHelper = sqlHelper;
    }

    public void setIdGenerator(IDGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }
}

package message.mybatis.common.mapper;

import message.base.utils.ApplicationHelper;
import message.mybatis.helper.SqlHelper;
import message.mybatis.common.dao.BaseRepository;
import message.mybatis.common.provider.EmptyMapperProvider;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 处理主要逻辑，最关键的一个类.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/7/13 下午3:39
 */
public class MapperHelper {
    /**
     * 注册的通用Mapper接口
     */
    private Map<Class<?>, MapperTemplate> registerMapper = new ConcurrentHashMap<>();

    /**
     * 缓存msid和MapperTemplate
     */
    private Map<String, MapperTemplate> msIdCache = new HashMap<>();
    /**
     * 缓存skip结果
     */
    private final Map<String, Boolean> msIdSkip = new HashMap<>();
    /**
     * sqlHelper
     */
    private SqlHelper sqlHelper;

    /**
     * 默认构造方法
     */
    public MapperHelper() {
    }

    /**
     * 带配置的构造方法
     *
     * @param properties
     */
    public MapperHelper(Properties properties) {
        setProperties(properties);
    }

    /**
     * 通过通用Mapper接口获取对应的MapperTemplate
     *
     * @param mapperClass
     * @return
     * @throws Exception
     */
    private MapperTemplate fromMapperClass(Class<?> mapperClass) {
        Method[] methods = mapperClass.getDeclaredMethods();
        Class<?> templateClass = null;
        Class<?> tempClass = null;
        Set<String> methodSet = new HashSet<String>();
        for (Method method : methods) {
            if (method.isAnnotationPresent(SelectProvider.class)) {
                SelectProvider provider = method.getAnnotation(SelectProvider.class);
                tempClass = provider.type();
                methodSet.add(method.getName());
            } else if (method.isAnnotationPresent(InsertProvider.class)) {
                InsertProvider provider = method.getAnnotation(InsertProvider.class);
                tempClass = provider.type();
                methodSet.add(method.getName());
            } else if (method.isAnnotationPresent(DeleteProvider.class)) {
                DeleteProvider provider = method.getAnnotation(DeleteProvider.class);
                tempClass = provider.type();
                methodSet.add(method.getName());
            } else if (method.isAnnotationPresent(UpdateProvider.class)) {
                UpdateProvider provider = method.getAnnotation(UpdateProvider.class);
                tempClass = provider.type();
                methodSet.add(method.getName());
            }
            if (templateClass == null) {
                templateClass = tempClass;
            } else if (templateClass != tempClass) {
                throw new RuntimeException("一个通用Mapper中只允许存在一个MapperTemplate子类!");
            }
        }
        if (templateClass == null || !MapperTemplate.class.isAssignableFrom(templateClass)) {
            templateClass = EmptyMapperProvider.class;
        }
        MapperTemplate mapperTemplate;
        try {
            mapperTemplate = (MapperTemplate) templateClass.getConstructor(Class.class, MapperHelper.class).newInstance(mapperClass, this);
        } catch (Exception e) {
            throw new RuntimeException("实例化MapperTemplate对象失败:" + e.getMessage());
        }
        //注册方法
        for (String methodName : methodSet) {
            try {
                mapperTemplate.addMethodMap(methodName, templateClass.getMethod(methodName, MappedStatement.class));
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(templateClass.getCanonicalName() + "中缺少" + methodName + "方法!");
            }
        }
        return mapperTemplate;
    }

    /**
     * 注册通用Mapper接口
     *
     * @param mapperClass
     * @throws Exception
     */
    public void registerMapper(Class<?> mapperClass) {
        if (!registerMapper.containsKey(mapperClass)) {
            registerMapper.put(mapperClass, fromMapperClass(mapperClass));
        }
        //自动注册继承的接口
        Class<?>[] interfaces = mapperClass.getInterfaces();
        if (interfaces != null && interfaces.length > 0) {
            for (Class<?> anInterface : interfaces) {
                registerMapper(anInterface);
            }
        }
    }

    /**
     * 注册通用Mapper接口
     *
     * @param mapperClass
     * @throws Exception
     */
    public void registerMapper(String mapperClass) {
        try {
            registerMapper(Class.forName(mapperClass));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("注册通用Mapper[" + mapperClass + "]失败，找不到该通用Mapper!");
        }
    }

    /**
     * 获取表名
     *
     * @param entityClass
     * @return
     */
    public String getTableName(Class<?> entityClass) {
        EntityHelper.EntityTable entityTable = EntityHelper.getEntityTable(entityClass);
        return entityTable.getName();
    }

    /**
     * 判断当前的接口方法是否需要进行拦截
     *
     * @param msId
     * @return
     */
    public boolean isMapperMethod(String msId) {
        if (msIdSkip.get(msId) != null) {
            return msIdSkip.get(msId);
        }
        for (Map.Entry<Class<?>, MapperTemplate> entry : registerMapper.entrySet()) {
            if (entry.getValue().supportMethod(msId)) {
                msIdSkip.put(msId, true);
                return true;
            }
        }
        msIdSkip.put(msId, false);
        return false;
    }

    /**
     * 获取MapperTemplate
     *
     * @param msId
     * @return
     */
    private MapperTemplate getMapperTemplate(String msId) {
        MapperTemplate mapperTemplate = null;
        if (msIdCache.get(msId) != null) {
            mapperTemplate = msIdCache.get(msId);
        } else {
            for (Map.Entry<Class<?>, MapperTemplate> entry : registerMapper.entrySet()) {
                if (entry.getValue().supportMethod(msId)) {
                    mapperTemplate = entry.getValue();
                    break;
                }
            }
            msIdCache.put(msId, mapperTemplate);
        }
        return mapperTemplate;
    }

    /**
     * 重新设置SqlSource
     *
     * @param ms
     */
    public void setSqlSource(MappedStatement ms) {
        MapperTemplate mapperTemplate = getMapperTemplate(ms.getId());
        try {
            if (mapperTemplate != null) {
                mapperTemplate.setSqlSource(ms);
            }
        } catch (Exception e) {
            throw new RuntimeException("调用方法异常:" + e.getMessage());
        }
    }

    /**
     * 配置属性
     *
     * @param properties
     */
    public void setProperties(Properties properties) {
        if (properties == null) {
            return;
        }
        //注册通用接口
        String mapper = properties.getProperty("mappers");
        if (mapper != null && mapper.length() > 0) {
            String[] mappers = mapper.split(",");
            for (String mapperClass : mappers) {
                if (mapperClass.length() > 0) {
                    registerMapper(mapperClass);
                }
            }
        }
    }

    /**
     * 从MappedStatement的参数中指定类型的参数
     *
     * @param parameter
     * @param target
     * @param <T>
     * @return
     */
    public <T> T findObjectFromParameter(Object parameter, Class<T> target) {
        if (parameter == null || target == null) {
            return null;
        }

        if (target.isAssignableFrom(parameter.getClass())) {
            return (T) parameter;
        }

        if (parameter instanceof MapperMethod.ParamMap) {
            MapperMethod.ParamMap<Object> paramMap = (MapperMethod.ParamMap<Object>) parameter;
            for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
                Object paramValue = entry.getValue();

                if (paramValue != null && target.isAssignableFrom(paramValue.getClass())) {
                    return (T) paramValue;
                }
            }
        }

        return null;
    }

    /**
     * 获取返回值类型 - 实体类型
     *
     * @param ms
     * @return
     */
    public Class<?> getSelectReturnType(MappedStatement ms) {
        String msId = ms.getId();
        Class<?> mapperClass = getMapperClass(msId);
        Type[] types = mapperClass.getGenericInterfaces();
        for (Type type : types) {
            if (type instanceof ParameterizedType) {
                ParameterizedType t = (ParameterizedType) type;
                if (t.getRawType() == BaseRepository.class || BaseRepository.class.isAssignableFrom((Class<?>) t.getRawType())) {
                    Class<?> returnType = (Class<?>) t.getActualTypeArguments()[0];
                    return returnType;
                }
            }
        }
        throw new RuntimeException("无法获取Mapper<T>泛型类型:" + msId);
    }

    /**
     * 根据msId获取接口类
     *
     * @param msId
     * @return
     * @throws ClassNotFoundException
     */
    public Class<?> getMapperClass(String msId) {
        if (msId.indexOf(".") == -1) {
            throw new RuntimeException("当前MappedStatement的id=" + msId + ",不符合MappedStatement的规则!");
        }
        String mapperClassStr = msId.substring(0, msId.lastIndexOf("."));
        try {
            return Class.forName(mapperClassStr);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    /**
     * 根据给定的sequence name设置主键值
     *
     * @param obj
     */
    public void setPkIdValue(Object obj) {
        if (obj == null) {
            return;
        }

        Class<?> clazz = obj.getClass();

        EntityHelper.EntityTable table = EntityHelper.getEntityTable(clazz);
        Set<EntityHelper.EntityColumn> keys = table.getEntityClassPKColumns();
        for (EntityHelper.EntityColumn key : keys) {
            String property = key.getProperty();
            String generator = key.getGenerator();

            if(this.getValue(obj, property) != null) {
                continue;
            }

            if (this.sqlHelper == null) {
                this.sqlHelper = ApplicationHelper.getInstance().getBean(SqlHelper.class);
            }

            Long pkId = this.sqlHelper.getNextId(generator);
            this.setValue(obj, property, pkId);
        }
    }

    private void setValue(Object obj, String fieldName, Object value) {
        MetaObject msObject = SystemMetaObject.forObject(obj);
        msObject.setValue(fieldName, value);
    }

    private Object getValue(Object obj, String fieldName) {
        MetaObject msObject = SystemMetaObject.forObject(obj);
        return msObject.getValue(fieldName);
    }
}
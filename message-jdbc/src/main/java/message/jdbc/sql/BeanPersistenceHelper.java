package message.jdbc.sql;

import message.jdbc.sql.BeanPersistenceBuilder;
import message.jdbc.sql.BeanPersistenceDef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


/**
 * 构建bean与数据库表关系的工具类.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 2012-10-8 上午04:18:37
 */
public class BeanPersistenceHelper {
    private static final Logger logger = LoggerFactory.getLogger(BeanPersistenceHelper.class);
    //BeanPersistenceDef的存储对象,类似缓存
    private static final Map<Class<?>, BeanPersistenceDef> beanPersistenceMaps = new HashMap<Class<?>, BeanPersistenceDef>();
    //同步锁
    private static final Object lockObject = new Object();

    private BeanPersistenceHelper() {
    }

    /**
     * get bean persistence definition for clazz
     *
     * @param clazz
     * @return
     */
    public static BeanPersistenceDef getBeanPersistenceDef(Class<?> clazz) throws Exception {
        BeanPersistenceDef beanPersistence = beanPersistenceMaps.get(clazz);
        if (beanPersistence == null) {
            beanPersistence = initialize(clazz);
        }

        return beanPersistence;
    }

    /**
     * initialize this clazz, return bean persistence definition
     *
     * @param clazz
     * @return
     */
    private static BeanPersistenceDef initialize(Class<?> clazz) throws Exception {
        BeanPersistenceDef beanPersistence = null;
        synchronized (lockObject) {
            logger.debug("beanPersistence is null, begin build it for '{}'!", clazz);
            BeanPersistenceBuilder beanPersistenceBuilder = new BeanPersistenceBuilder(clazz);
            beanPersistence = beanPersistenceBuilder.build();
            logger.debug("build beanPersistence is '{}'!", beanPersistence);
            if (beanPersistence != null) {
                beanPersistenceMaps.put(clazz, beanPersistence);
            }
        }

        return beanPersistence;
    }
}

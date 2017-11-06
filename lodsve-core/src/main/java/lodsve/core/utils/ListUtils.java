package lodsve.core.utils;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:stormning@163.com">stormning</a>
 * @version V1.0, 2015/8/17.
 */
public class ListUtils {

    /**
     * 根据key查询
     *
     * @param <T> key类型
     * @param <K> value类型
     */
    public interface KeyFinder<T, K> {
        /**
         * 根据key查询
         *
         * @param target key
         * @return value
         */
        K findKey(T target);
    }

    /**
     * 判断是否符合条件
     *
     * @param <T> 集合元素类型
     */
    public interface Decide<T> {
        /**
         * 判断
         *
         * @param target 待判断对象
         * @return true/false
         */
        boolean judge(T target);
    }

    /**
     * 将一个对象集合转成另外一个对象的集合
     *
     * @param <K> 原集合元素类型
     * @param <T> 新集合元素类型
     */
    public interface Transform<K, T> {
        /**
         * 单个元素转换
         *
         * @param target 原集合元素
         * @return 新集合元素
         */
        T transform(K target);
    }

    public static <K, T> Map<K, T> toMap(Collection<T> targets, KeyFinder<T, K> keyFinder) {
        if (targets == null) {
            return Collections.emptyMap();
        }
        Map<K, T> result = new HashMap<>(targets.size());
        for (T target : targets) {
            result.put(keyFinder.findKey(target), target);
        }
        return result;
    }

    public static <T> T findOne(Collection<T> targets, Decide decide) {
        if (CollectionUtils.isEmpty(targets)) {
            return null;
        }

        for (T target : targets) {
            if (decide.judge(target)) {
                return target;
            }
        }

        return null;
    }

    public static <T> List<T> findMore(Collection<T> targets, Decide decide) {
        List<T> result = new ArrayList<>();

        if (CollectionUtils.isEmpty(targets)) {
            return result;
        }

        for (T target : targets) {
            if (decide.judge(target)) {
                result.add(target);
            }
        }

        return result;
    }

    public static <K, T> List<T> transform(Collection<K> targets, Transform<K, T> transform) {
        if (CollectionUtils.isEmpty(targets)) {
            return Collections.emptyList();
        }

        List<T> result = new ArrayList<>(targets.size());
        for (K k : targets) {
            result.add(transform.transform(k));
        }

        return result;
    }
}

package message.mvc.resolver;

import message.base.utils.ApplicationHelper;
import message.mvc.annotation.Resolver;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 解析参数的工厂类，只要存储要参数类型和解析类的关系.<br/>
 * <code>@Resolver(WebInput.class)</code> --> 标注是解析类，并且解析参数类型是WebInput<br/>
 * <code>@Component</code>                --> spring容器托管
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 2014-12-19 13:30
 */
public class MethodArgumentResolverFactory {
    private static MethodArgumentResolverFactory factory;
    private static Map<Class<?>, HandlerResolver> handlerResolverMap = new HashMap<Class<?>, HandlerResolver>();

    public static <T> HandlerResolver<T> getResolver(Class<T> clazz){
        if(factory == null) {
            factory = new MethodArgumentResolverFactory();
            Collection<HandlerResolver> resolverSet = ApplicationHelper.getInstance().getBeansByType(HandlerResolver.class).values();
            for(HandlerResolver resolver : resolverSet){
                Resolver resAnnot = resolver.getClass().getAnnotation(Resolver.class);
                if(resAnnot == null) {
                    continue;
                }

                handlerResolverMap.put(resAnnot.value(), resolver);
            }
        }

        return handlerResolverMap.get(clazz);
    }
}

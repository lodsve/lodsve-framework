package message.web.core;

import message.base.utils.ApplicationHelper;
import message.utils.DateFormat;
import message.utils.RequestUtils;
import message.utils.StringUtils;
import message.web.annontations.Controller;
import message.web.annontations.UrlMapping;
import message.web.commons.BeanHandler;
import message.web.commons.BeanHandlerFactory;
import message.web.commons.FileWebInput;
import message.web.commons.WebInput;
import message.web.commons.WebOutput;
import message.web.exception.NoSuchControllerDefinationException;
import message.web.expand.CustomDateEditor;
import message.web.expand.ModelAndMethod;
import message.web.script.ScriptObjectResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;
import org.springframework.web.util.NestedServletException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * Controller Forward Center.控制器转发中心.
 * 
 * @author Danny(sunhao)(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 2012-10-16 下午08:36:28
 */
public class ControllerForwardCenter extends ApplicationObjectSupport implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(ControllerForwardCenter.class);
	
	/** Default command name used for binding command objects: "command" */
	public static final String DEFAULT_COMMAND_NAME = "command";
	/**
	 * controller配置文件(moduleName = className)
	 */
	private List<?> controllerConfigFiles;
	/**
	 * controller中注入参数(className = beanName)<br/>
	 * 基于注解Autowired
	 */
	private List<?> parameterConfigFiles;
	/**
	 * 将controller的class,对象组建成BeanHandler,放入list中
	 */
	private List<BeanHandler> beanHandlers;
	/**
	 * controller的BeanHandler集合(moudle--BeanHandler)
	 */
	private Map<String, Object> beanHandlerMap;
	/**
	 * 将controller中Autowired注解的字段的beanName,class组建成BeanHandler,放入list中
	 */
	private List<BeanHandler> beanParameterHandlers;
	/**
	 * controller中Autowired注解的字段的实现类className--beanName
	 */
	private Map<String, Object> beanParameterMap;
	/**
	 * 已初始化的controller的className集合
	 */
	private List<String> initedObject;
	/**
	 * 是否可以断点调试
	 */
	private boolean debug;
    /**
     * class的脚本解析器
     */
	private ScriptObjectResolver objectResolver;
    /**
     * 需要扫描的包路径(可以配置多个)
     */
    private List<String> scanPackages;
    /**
     * 需要扫描的包路径(只可以配置一个)
     */
    private String scanPackage;

    /**
     * 通配符的Resource查找器
     */
    private ResourcePatternResolver resourcePatternResolver;

    /**
     * 类-->(url-->method)的关系
     */
    private Map<Class<?>, Map<String, Method>> methodDoMappings;
	
	/**
	 * 构造器,私有化构造器,外部不能直接实例化
	 */
	private ControllerForwardCenter() {
		this.beanParameterMap = new ConcurrentHashMap();
		this.initedObject = new ArrayList();
		this.debug = false;
        /**通配符的Resource查找器--通过path路径查找的通配符的Resource查找器**/
        this.resourcePatternResolver = new PathMatchingResourcePatternResolver();
	}

    public void afterPropertiesSet() throws Exception {
        if(this.beanHandlers == null)
            this.beanHandlers = new ArrayList();

        if(this.beanParameterHandlers == null)
            this.beanParameterHandlers = new ArrayList();

        if(this.beanHandlerMap == null)
            this.beanHandlerMap = new HashMap();

        initControllerParameters();
        initController();
    }
	/**
	 * 系统启动后,只有一个ControllerProcess对象存在于上下文中
	 * 
	 * @return
	 */
	public static ControllerForwardCenter getInstance() {
        return (ControllerForwardCenter) ApplicationHelper.getInstance().getBean("controllerForwardCenter");
    }
	
	/******************************************************系统URL映射初始化--开始***********************************************************/
	
	/**
	 * 开始初始化controller
	 */
	private void initController(){
		Map properties = this.getPropertiesFromConfigs(this.controllerConfigFiles);
        properties.putAll(this.getPropertiesFromScanPackages());
		Map handleMap = this.initHandlers(properties);
		
		for(Iterator it = properties.entrySet().iterator(); it.hasNext(); ){
			Entry entry = (Entry) it.next();
			String moduleName = (String) entry.getKey();
            Object value = entry.getValue();

			try {
				Class clazz = null;
                String className = StringUtils.EMPTY;
                if(value instanceof String){
                    clazz = Class.forName((String) value);
                    className = (String) value;
                } else if(value instanceof Class){
                    clazz = (Class) value;
                    className = ((Class) value).getName();
                }

                if(logger.isDebugEnabled())
                    logger.debug("load controller: module is '{}', class is '{}'!", new Object[]{moduleName, className});

				BeanHandler bh = new BeanHandler();
				bh.setClazz(clazz);
				bh.setValue(handleMap.get(className));
				
				this.beanHandlerMap.put(moduleName, handleMap.get(className));
				this.beanHandlers.add(bh);
			} catch (ClassNotFoundException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}
	
	/**
	 * 初始化parameterConfigFiles文件,将K-V键值对放入BeanHandler中
	 */
	private void initControllerParameters(){
		Map map = this.getPropertiesFromConfigs(this.parameterConfigFiles);

		Iterator it = map.entrySet().iterator();
		while (it.hasNext()) {
			Entry entry = (Entry) it.next();
			String className = StringUtils.trim((String) entry.getKey());
			String beanName = StringUtils.trim((String) entry.getValue());
			this.beanParameterMap.put(className, beanName);
			if (logger.isDebugEnabled())
				logger.debug("load beanName is '{}' class is '{}'!", new Object[] { className, beanName });

			try {
				Class clazz = Class.forName(className);
				BeanHandler bh = new BeanHandler();
				bh.setClazz(clazz);
				bh.setName(beanName);

				this.beanParameterHandlers.add(bh);
			} catch (ClassNotFoundException e) {
				logger.debug(e.getMessage());
			}
		}
	}
	
	/**
	 * 从properties文件中读取K-V关系,返回一个Map
	 * 
	 * @param files
	 * @return
	 */
	private Map getPropertiesFromConfigs(List files){
		if(files == null || files.isEmpty()){
			logger.debug("this files is null!");
			return Collections.EMPTY_MAP;
		}
		Map map = new HashMap();
		Iterator it = files.iterator();
		while(it.hasNext()){
			String file = (String) it.next();
			Resource rc = this.getApplicationContext().getResource(file);
			if(rc == null){
				logger.debug("'{}' is not found!", file);
				continue;
			}
			
			Properties p = new Properties();
			try {
				p.load(rc.getInputStream());
			} catch (IOException e) {
				logger.error(e.getMessage());
				continue;
			}
			
			if(p.isEmpty())
				continue;
			
			map.putAll(p);
		}
		
		return map;
	}

    private Map getPropertiesFromScanPackages(){
        initScanPackage();
        Map result = new HashMap();
        if(this.scanPackages == null || this.scanPackages.isEmpty())
            return result;

        try {
            for(Iterator<String> it = this.scanPackages.iterator(); it.hasNext(); ){
                String spk = it.next();
                String pattern = "classpath*:" + ClassUtils.convertClassNameToResourcePath(spk) + "/**/*.class";

                Resource[] resources = this.resourcePatternResolver.getResources(pattern);
                MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(this.resourcePatternResolver);

                for(Resource r : resources){
                    if(r.isReadable()){
                        MetadataReader reader = readerFactory.getMetadataReader(r);
                        String classNameWithPackage = reader.getClassMetadata().getClassName();
                        Class clazz = ClassUtils.forName(classNameWithPackage, Thread.currentThread().getContextClassLoader());

                        if(!clazz.isAnnotationPresent(Controller.class))
                            continue;

                        Controller c = (Controller) clazz.getAnnotation(Controller.class);
                        String className = c.name();

                        if(StringUtils.isEmpty(className)){
                            String[] nameTemp = StringUtils.split(classNameWithPackage, ".");
                            className = StringUtils.remove(nameTemp[nameTemp.length - 1], "Controller");
                            className = className.substring(0, 1).toLowerCase() + className.substring(1);
                        }

                        result.put(className, clazz);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 初始化包路径的配置
     */
    private void initScanPackage(){
        if(this.scanPackages == null)
            this.scanPackages = new ArrayList<String>();

        if(StringUtils.isNotEmpty(this.scanPackage))
            this.scanPackages.add(this.scanPackage);
    }
	
	/**
	 * 初始化controller
	 * 
	 * @param map		配置文件XXX-controller.properties中的键值对
	 * @return
	 */
	private Map<String, Object> initHandlers(Map map){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Iterator it = map.entrySet().iterator();
		while(it.hasNext()){
			Class clazz = null;
			Entry entry = (Entry) it.next();
            Object value = entry.getValue();

            if(value instanceof String){
                try {
                    clazz = Class.forName((String) value);
                } catch (ClassNotFoundException e) {
                    logger.error("create class '{}' is error!", (String) value);
                }
            } else if(value instanceof Class){
                clazz = (Class) value;
            }

            Object controller = null;
            if(clazz != null)
                controller = BeanUtils.instantiateClass(clazz);

			if(controller != null){
				initObjectParam(controller);
				resultMap.put(clazz.getName(), controller);
			}
		}
		
		return resultMap;
	}
	
	/**
	 * 初始化controller基于Autowired注解的字段的注入
	 * 
	 * @param controller
	 */
	private void initObjectParam(Object controller){
		Class clazz = controller.getClass();
		if(this.initedObject.contains(clazz.getName()))
			return;
		
		Field[] fields = clazz.getDeclaredFields();
		for(Field f : fields){
			if(f != null && f.isAnnotationPresent(Autowired.class))
				initField(f, controller);
		}
		
		this.initedObject.add(clazz.getName());
	}
	
	/**
	 * 为controller中Autowired的字段设值
	 * 
	 * @param field					字段
	 * @param controller			controller
	 */
	private void initField(Field field, Object controller) {
		Class fieldClass = field.getType();
		Object find = this.getBean(fieldClass);
		field.setAccessible(true);
		try {
			field.set(controller, find);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取基于注解的字段真正的实现类对象(beanParameterHandlers中和context系统上下文中)
	 * 
	 * @param classObj
	 * @return
	 */
	private Object getBean(Class classObj){
		ApplicationContext ac = this.getApplicationContext();
		Object returnObj = null;
		try{
			if(classObj.equals(ApplicationContext.class)){
				returnObj = this.getApplicationContext();
			} else {
				if(this.beanParameterHandlers != null && this.beanParameterHandlers.size() > 0){
					for(Iterator it = this.beanParameterHandlers.iterator(); it.hasNext(); ){
						BeanHandler bh = (BeanHandler) it.next();
						if(classObj.equals(bh.getClazz())){
							if(StringUtils.isNotEmpty(bh.getName())){
								returnObj = ac.getBean(bh.getName());
							} else {
								returnObj = bh.getValue();
							}
						}
					}
				}
			}
			if(returnObj == null)
				returnObj = ac.getBean(classObj);
			
		} catch(Exception e){
			logger.error(e.getMessage(), e);
		}
		
		return returnObj;
	}
	
	/******************************************************系统URL映射初始化--结束***********************************************************/
	/************************************************************神奇的分隔符****************************************************************/
	/*******************************************************系统解析URL映射--开始************************************************************/
	
	/**
     * 解析URL映射<br/>
     * eg. <br/>
     * <ul>
     * <li>
     * 1.URL: ${contextPath}/album/index.do,则:module == album; url == index
     * </li>
     * <li>
     * 2.URL: ${contextPath}/album.do,则:module == album; url == do<br/>
     * 注意:如果以这种情况出现,则默认将url == do解析为url == index
     * </li>
     * </ul>
     *
     * @param moduleName			模块名
     * @param url			        请求.do名
     * @param request
     * @param response
     * @return						ModelAndView,返回相应的视图
     * @throws Exception
     */
	public ModelAndView invoke(String moduleName, String url, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if("do".equals(url)){
			//如果url为do,则解析为index
            url = "index";
		}
		Object controller = this.getBeanHandler(moduleName, url, request, response);
		int invokeDepth = 0;
		//上下文
		request.setAttribute("APPLICATION_CONTEXT", this.getApplicationContext());
		Object returnValue = null;
        Map modelMap = new HashMap();
        Object invokeObject = controller;
        String invokeModuleName = moduleName;
        
        while (true) {
        	invokeDepth++;
        	returnValue = invokeObjectNamedMethod(invokeObject, url, request, response, invokeDepth);
        	
        	if(returnValue == null)
        		return null;
        	
        	if(returnValue instanceof ModelAndMethod){
        		ModelAndMethod mam = (ModelAndMethod) returnValue;
        		Map params = mam.getParams();
                if (params != null) {
                    modelMap.putAll(params);
                }

                ModelAndView mav = mam.getModelAndView();
                if (mav != null) {
                    returnValue = mav;
                }
                url = mam.getMethod();
                invokeModuleName = mam.getModule();
                
                if(StringUtils.isEmpty(invokeModuleName)){
                	invokeModuleName = this.getModuleByBeanClass(invokeObject.getClass());
                }
                
                if (invokeModuleName != null)
                    invokeObject = this.getBeanHandler(invokeModuleName, url, request, response);
                
        	} else if(returnValue instanceof Map){
        		Map params = (Map) returnValue;
                String moduleAndMethodName = (String) params.get("module.url");
                if (moduleAndMethodName != null) {
                    String[] names = StringUtils.split(moduleAndMethodName, '.');
                    if (names != null && names.length == 2) {
                        invokeModuleName = names[0];
                        url = names[1];
                        if (invokeModuleName != null) {
                            params.remove("module.url");
                            Iterator it = params.entrySet().iterator();
                            while (it.hasNext()) {
                                Entry e = (Entry) it.next();
                                request.setAttribute((String) e.getKey(), e.getValue());
                            }

                            invokeObject = this.getBeanHandler(invokeModuleName, url, request, response);
                        }

                        continue;
                    }
                }

                String vName = (String) params.get("view.name");
                if (vName == null) 
                	vName = moduleName + "." + url;
                return new ModelAndView(vName, params);
        	} else {
        		ModelAndView mv = (ModelAndView) returnValue;
                mv.addAllObjects(modelMap);
                break;
        	}
        }
		
		return massageReturnValueIfNecessary(returnValue);
	}
	
	/**
	 * Processes the return value of a handler method to ensure that it either returns
	 * <code>null</code> or an instance of {@link ModelAndView}. When returning a {@link java.util.Map},
	 * the {@link java.util.Map} instance is wrapped in a new {@link ModelAndView} instance.
	 */
	public ModelAndView massageReturnValueIfNecessary(Object returnValue) {
		if (returnValue instanceof ModelAndView) {
			return (ModelAndView) returnValue;
		} else if (returnValue instanceof Map) {
			return new ModelAndView().addAllObjects((Map) returnValue);
		} else {
			// Either returned null or was 'void' return.
			// We'll assume that the handle method already wrote the response.
			return null;
		}
	}
	
	/**
	 * 执行这次请求
	 * 
	 * @param obj					请求执行的controller
	 * @param url			        本次请求的.do
	 * @param request
	 * @param response
	 * @param invokeDepth			执行次数(<10)
	 * @return
	 * @throws Exception
	 */
	protected final Object invokeObjectNamedMethod(Object obj, String url, HttpServletRequest request,
			HttpServletResponse response, int invokeDepth) throws Exception {
		if(invokeDepth > 10)
			throw new ServletRequestBindingException("request too much times!not most than 10 times!");

		Method method = this.getMethodByUrl(obj.getClass(), url);

		if (method == null)
			throw new NoSuchRequestHandlingMethodException(url, obj.getClass());

		if (logger.isDebugEnabled())
			logger.debug("get method '{}' for url '{}'!", new Object[] {method, url });
		 
		try {
			List<Object> params = new ArrayList<Object>();
			Class[] paramsClass = method.getParameterTypes();
			BindException be = null;
			
			for (int i = 0; i < paramsClass.length; i++) {
				Class c = paramsClass[i];
				if (HttpServletRequest.class.equals(c)) {
					params.add(request);
				} else if (HttpServletResponse.class.equals(c)) {
					params.add(response);
				} else if (HttpSession.class.equals(c)) {
					params.add(request.getSession());
				} else if (WebInput.class.equals(c)) {
					params.add(new WebInput(request));
				} else if (WebOutput.class.equals(c)) {
					params.add(new WebOutput(request, response));
				} else if (FileWebInput.class.equals(c)) {
					params.add(new FileWebInput(request));
				} else if (ApplicationContext.class.equals(c)) {
					params.add(this.getApplicationContext());
				} else {
					Object param = null;
                    BeanHandlerFactory beanHandlerFactory = null;
					if (this.beanParameterHandlers != null && this.beanParameterHandlers.size() > 0) {
						Iterator it = beanParameterHandlers.iterator();
						while (it.hasNext()) {
							BeanHandler bh = (BeanHandler) it.next();
							if (bh.getClazz().equals(c)) {
								if (StringUtils.isNotEmpty(bh.getName())) {
									param = this.getApplicationContext().getBean(bh.getName());
								} else {
									param = bh.getValue();
								}
								if (param instanceof BeanHandlerFactory) {
                                    beanHandlerFactory = (BeanHandlerFactory) param;
									param = beanHandlerFactory.getValue(request, response);
								}

								break;
							}
						}
					}

                    if(beanHandlerFactory == null){
                        Object command = newCommandObject(c);
                        try {
                            bind(request, command);
                            param = command;
                        } catch (BindException e) {
                            be = e;
                        }
                    }

                    params.add(param);
				}
			}
			
			if(be != null){
				if (BindException.class.equals(paramsClass[paramsClass.length - 1])) {
                    params.add(be);
                } else {
                    throw new ServletRequestBindingException("Errors binding onto object '" + be.getObjectName() + "'", be);
                }
			}
			
			return method.invoke(obj, params.toArray(new Object[params.size()]));
		} catch (Throwable e) {
			if(logger.isErrorEnabled())
				logger.error(e.getMessage());
			
			if (e instanceof Exception) {
	            throw (Exception) e;
	        }
	        if (e instanceof Error) {
	            throw (Error) e;
	        }
	        // Should never happen!
	        throw new NestedServletException("Unknown Throwable type encountered", e);
		}
	}

    /**
     * 根据url获取controller中对应的方法名
     *
     * @param controller
     * @param url
     * @return
     */
    private Method getMethodByUrl(Class<?> controller, String url){
        if(this.methodDoMappings == null)
            this.methodDoMappings = new HashMap<Class<?>, Map<String, Method>>();

        Map<String, Method> urlMethods = this.methodDoMappings.get(controller);
        if(urlMethods == null){
            initMethodUrl(controller);
            urlMethods = this.methodDoMappings.get(controller);
        }

        return urlMethods.get(url);
    }

    /**
     * 初始化指定controller中的url和method的对应关系
     *
     * @param controller
     */
    private void initMethodUrl(Class<?> controller){
        Method[] methods = controller.getMethods();
        Map<String, Method> urlMethod = new HashMap<String, Method>();
        for (Method m : methods) {
            if (ModelAndView.class.equals(m.getReturnType()) || Map.class.equals(m.getReturnType()) || ModelAndMethod.class.equals(m.getReturnType())){
                String url = m.getName();
                UrlMapping um = m.getAnnotation(UrlMapping.class);
                if(um != null)
                    url = StringUtils.isNotEmpty(um.value()) ? um.value() : m.getName();

                urlMethod.put(url, m);
            }
        }

        this.methodDoMappings.put(controller, urlMethod);
    }
	 
	/**
	 * Create a new command object of the given class.
	 * <p>This implementation uses <code>BeanUtils.instantiateClass</code>, so
	 * commands need to have public no-arg constructors. Subclasses can override
	 * this implementation if desired.
	 * 
	 * @throws Exception if the command object could not be instantiated
	 * @see org.springframework.beans.BeanUtils#instantiateClass(Class)
	 */
	public Object newCommandObject(Class clazz) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("Creating new command of class [" + clazz.getName() + "]");
		}
		return BeanUtils.instantiateClass(clazz);
	}
	
	/**
	 * Bind request parameters onto the given command bean
	 * @param request request from which parameters will be bound
	 * @param command command object, that must be a JavaBean
	 * @throws Exception in case of invalid state or arguments
	 */
	public void bind(HttpServletRequest request, Object command) throws Exception {
		logger.debug("Binding request parameters onto MultiActionController command");
		ServletRequestDataBinder binder = createBinder(request, command);
		
		binder.bind(request);
	}
	
	/**
	 * Create a new binder instance for the given command and request.
	 * <p>Called by <code>bind</code>. Can be overridden to plug in custom
	 * ServletRequestDataBinder subclasses.
	 * <p>The default implementation creates a standard ServletRequestDataBinder,
	 * and invokes <code>initBinder</code>. Note that <code>initBinder</code>
	 * will not be invoked if you override this method!
	 * @param request current HTTP request
	 * @param command the command to bind onto
	 * @return the new binder instance
	 * @throws Exception in case of invalid state or arguments
	 * @see #bind
	 */
	public ServletRequestDataBinder createBinder(HttpServletRequest request, Object command) throws Exception {
		List<String> params = RequestUtils.getRequestParam(request);
        ServletRequestDataBinder binder = new ServletRequestDataBinder(command, DEFAULT_COMMAND_NAME);

        DateFormat dateFormat1 = new DateFormat(getDateFormatPattern(params));
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat1, true));

		return binder;
	}
	
	/**
	 * get date format pattern for what parameter in request
	 * @param urlParams
	 * @return
	 */
	private String getDateFormatPattern(List<String> urlParams){
        if(urlParams.isEmpty()){
            logger.debug("this url paramter is null!");
            return "yyyy-MM-dd HH:mm";
        }
        //yyyy-MM-dd hh:mm
        Pattern p1 = Pattern.compile("(\\d){2,4}[-](\\d){1,2}[-](\\d){1,2} (\\d){1,2}[:](\\d){1,2}");
        //yyyy-MM-dd
        Pattern p2 = Pattern.compile("(\\d){2,4}[-](\\d){1,2}[-](\\d){1,2}");
        //hh:mm yyyy-MM-dd
        Pattern p3 = Pattern.compile("(\\d){1,2}[:](\\d){1,2} (\\d){2,4}[-](\\d){1,2}[-](\\d){1,2}");
        for(String up : urlParams){
            if(p1.matcher(up).matches()){
                return "yyyy-MM-dd HH:mm";
            }
            if(p2.matcher(up).matches()){
                return "yyyy-MM-dd";
            }
            if(p3.matcher(up).matches()){
                return "HH:mm yyyy-MM-dd";
            }
        }
        
        return "yyyy-MM-dd HH:mm";
    }
	
	/**
	 * 根据模块名称获取相对应的controller
	 * 
	 * @param moduleName			模块名称
	 * @param url			        .do名
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	private Object getBeanHandler(String moduleName, String url, HttpServletRequest request, HttpServletResponse response)
			throws Exception{
		Object resObj = this.beanHandlerMap.get(moduleName);
		
		if(!this.debug && this.objectResolver != null){
            //系统不能使用debug模式
			//解析字节码文件获取controller
			resObj = this.objectResolver.getObject(ClassUtils.getQualifiedName(resObj.getClass()));
			if(resObj == null){
				logger.error("given error module name '{}'!", moduleName);
				throw new NoSuchRequestHandlingMethodException(url, ControllerForwardCenter.class);
			}
			
			initObjectParam(resObj);
		}
		
		if(resObj == null){
			logger.error("given error module name '{}'!", moduleName);
			throw new NoSuchRequestHandlingMethodException(url, ControllerForwardCenter.class);
		}
		
		return resObj;
	}
	
	/**
	 * 通过controller的类名获取配置文件中的此controller的模块名
	 * 
	 * @param beanClass				controller的类名
	 * @return
	 */
	private String getModuleByBeanClass(Class beanClass){
		if(beanClass == null){
			logger.error("can't find module name for class '{}'!", beanClass);
			throw new NoSuchControllerDefinationException(10001, "can't find module controller for class '{" + beanClass.getName() + "}'!");
		}
		
		if(this.beanHandlerMap.isEmpty()){
			logger.warn("has no controller in application context!");
			return StringUtils.EMPTY;
		}
		
		String module = StringUtils.EMPTY;
		for(Iterator it = this.beanHandlerMap.entrySet().iterator(); it.hasNext(); ){
			Entry entry = (Entry) it.next();
			String moduleName = (String) entry.getKey();
			Object bean = entry.getValue();
			
			if(beanClass.equals(bean.getClass())){
				module = moduleName;
				break;
			}
		}
		if(StringUtils.isEmpty(module)){
			logger.error("can't find module name for class '{}'!", beanClass);
			throw new NoSuchControllerDefinationException(10001, "can't find module controller for class '{" + beanClass.getName() + "}'!");
		}
		
		return module;
	}
	
	
	/*******************************************************系统解析URL映射--结束************************************************************/

	public List<?> getControllerConfigFiles() {
		return controllerConfigFiles;
	}

	public void setControllerConfigFiles(List<?> controllerConfigFiles) {
		this.controllerConfigFiles = controllerConfigFiles;
	}

	public List<?> getParameterConfigFiles() {
		return parameterConfigFiles;
	}

	public void setParameterConfigFiles(List<?> parameterConfigFiles) {
		this.parameterConfigFiles = parameterConfigFiles;
	}

	public List<BeanHandler> getBeanHandlers() {
		return beanHandlers;
	}

	public void setBeanHandlers(List<BeanHandler> beanHandlers) {
		this.beanHandlers = beanHandlers;
	}

	public List<BeanHandler> getBeanParameterHandlers() {
		return beanParameterHandlers;
	}

	public void setBeanParameterHandlers(List<BeanHandler> beanParameterHandlers) {
		this.beanParameterHandlers = beanParameterHandlers;
	}

	public Map<String, Object> getBeanHandlerMap() {
		return beanHandlerMap;
	}

	public void setBeanHandlerMap(Map<String, Object> beanHandlerMap) {
		this.beanHandlerMap = beanHandlerMap;
	}

	public Map<String, Object> getBeanParameterMap() {
		return beanParameterMap;
	}

	public void setBeanParameterMap(Map<String, Object> beanParameterMap) {
		this.beanParameterMap = beanParameterMap;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public ScriptObjectResolver getObjectResolver() {
		return objectResolver;
	}

	public void setObjectResolver(ScriptObjectResolver objectResolver) {
		this.objectResolver = objectResolver;
	}

    public void setScanPackages(List<String> scanPackages) {
        this.scanPackages = scanPackages;
    }

    public void setScanPackage(String scanPackage) {
        this.scanPackage = scanPackage;
    }
}

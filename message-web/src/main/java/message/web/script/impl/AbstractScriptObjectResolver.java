package message.web.script.impl;

import message.base.utils.ApplicationHelper;
import message.web.script.ScriptNotFoundException;
import message.web.script.ScriptObjectResolver;
import message.web.script.ScriptUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 脚本文件解析类(父类,公共方法抽到这里).
 * 
 * @author Danny(sunhao)(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 2012-10-17 下午10:08:53
 */
public abstract class AbstractScriptObjectResolver implements ScriptObjectResolver, InitializingBean {
	private final static Logger logger = LoggerFactory.getLogger(AbstractScriptObjectResolver.class);
	
	protected String scriptPath;			//脚本文件存放的路径("webapp"/"scriptPath"/"package"/"class")
	protected String prefix;				//前缀
	protected String suffix;				//后缀
	protected boolean cache = true;			//是否缓存

	private Map<String, Object> cacheTimes;	//缓存脚本文件上一次修改时间
	private Map<String, Object> cacheObjs;	//缓存以前已解析过的对象

	/**
	 * 需要实现类实现自己的方法,抽象方法
	 */
	public abstract Object getObject(String name) throws Exception;
	
	/**
	 * 从指定的脚本文件解析出对象
	 * 
	 * @param name			脚本文件的文件名
	 * @return
	 * @throws Exception
	 */
	protected Object getObjectFromScript(String name) throws Exception{
		//将包名+类名解析成路径
		name = prepareName(name);
		String filePath = this.getScriptFilePath(name);
		
		File file = new File(filePath);
		if(!file.exists() || !file.canRead()){
			logger.error("file '{}' is not found!", filePath);
			throw new ScriptNotFoundException(10010, "script file '" + filePath + "' is not found!");
		}
		
		//上一次编辑时间
		long lastmodifiyTime = file.lastModified();
		
		Object obj = null;			//需要获取的controller
		if(this.cache){
			obj = this.cacheObjs.get(filePath);			//缓存在map中的
			if(obj != null){
				//这个文件上一次生成的时间
				long lastGetCacheTime = ((Long) this.cacheTimes.get(filePath)).longValue();
				if(lastmodifiyTime <= lastGetCacheTime)
					//如果在上一次缓存时间之后文件未被修改,则取缓存中的对象返回
					return obj;
			}
		}
		
		//从缓存中没有获取到对象
		try {
			obj = ScriptUtils.getObject(filePath);
		} catch (Exception e) {
			logger.error(e.getMessage());
			logger.error("file '{}' is not found!", filePath);
			throw new ScriptNotFoundException(10010, "script file '" + filePath + "' is not found!");
		}
		
		if(obj == null)
			throw new ScriptNotFoundException(10010, "script file '" + filePath + "' is not found!");
		
		if(obj != null && this.cache){
			//放入缓存
			this.cacheObjs.put(filePath, obj);
			this.cacheTimes.put(filePath, lastmodifiyTime);
		}
		
		return obj;
	}

	/**
	 * 将包名+类名解析成路径<br/>
	 * eg.<br/>
	 * 包名+类名:com.message.test.web.TestController<br/>
	 * 路径:com\message\test\web\TestController.class
	 * 
	 * @param name			包名+类名
	 * @return
	 */
	protected String prepareName(String name) {
        return name;
    }
	
	/**
	 * 获取脚本文件的完整路径
	 * 
	 * @param name
	 * @return
	 */
	protected String getScriptFilePath(String name) {
		StringBuffer path = new StringBuffer();
		path.append(ApplicationHelper.getInstance().getRootPath());
		path.append(this.scriptPath);
		path.append(StringUtils.trimToEmpty(this.prefix));
		path.append(name);
		path.append(StringUtils.trimToEmpty(this.suffix));
		
		return path.toString();
	}
	
	public void afterPropertiesSet() throws Exception {
		init();
	}
	
	/**
	 * 初始化
	 */
	protected void init(){
		this.cacheObjs = new HashMap<String, Object>();
		this.cacheTimes = new HashMap<String, Object>();
	}

	public String getScriptPath() {
		return scriptPath;
	}

	public void setScriptPath(String scriptPath) {
		this.scriptPath = scriptPath;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public boolean isCache() {
		return cache;
	}

	public void setCache(boolean cache) {
		this.cache = cache;
	}

	public Map<String, Object> getCacheTimes() {
		return cacheTimes;
	}

	public void setCacheTimes(Map<String, Object> cacheTimes) {
		this.cacheTimes = cacheTimes;
	}

	public Map<String, Object> getCacheObjs() {
		return cacheObjs;
	}

	public void setCacheObjs(Map<String, Object> cacheObjs) {
		this.cacheObjs = cacheObjs;
	}
}

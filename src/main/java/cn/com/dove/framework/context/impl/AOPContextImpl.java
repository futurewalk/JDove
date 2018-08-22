package cn.com.dove.framework.context.impl;

import cn.com.dove.anotation.*;
import cn.com.dove.framework.ApplicationFactory;
import cn.com.dove.framework.AspectReader;
import cn.com.dove.framework.IClassReader;
import cn.com.dove.framework.context.AOPContextService;
import cn.com.dove.framework.em.AspectType;
import cn.com.dove.helper.AspectContainer;
import cn.com.dove.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AOPContextImpl implements AOPContextService {
	 private static final Logger logger = LoggerFactory.getLogger(AOPContextImpl.class);
	 private static  Map<String,Object> aspectContainer = ApplicationFactory.getAspectContainer();

	 @Override
	 public <T> void registerBefore(String[] pathName,Method entryPoit) throws Throwable {

		  for(int index = 0;index < pathName.length;index++){
				//logger.info("获取当前的切入点:{}",pathName[index]);
				String pkName = pathName[index];
				boolean flg = false;
				if(pkName.endsWith(".*")) {
					 pkName = pkName.substring(0, pkName.lastIndexOf(".*"));
				}
				IClassReader classReader = new IClassReader() {
					 @Override
					 public <T> void read(Class<T> clazz, Map<String, Object> clazzMap) {
						  Controller controller = clazz.getAnnotation(Controller.class);
						  if (controller == null){
								return;
						  }
						  Method[] methods = clazz.getDeclaredMethods();
						  for(Method method:methods){
								ResMapping resMapping = method.getAnnotation(ResMapping.class);
								if (resMapping != null){
									 clazzMap.put(clazz.toString(),clazz);
								}
						  }
					 }
				};
				aspectContainer = FileUtils.getClasses(pkName,classReader, aspectContainer);

		  }




	 }

	 @Override
	 public <T> void registerAfter(String[] pathName,Method entryPoint){

	 }

	 @Override
	 public <T> void registerAround(String[] pathName,Method entryPoint) {

	 }

	 @Override
	 public <T> void registerThrowAfter(String[] pathName,Method entryPoint) {

	 }

	 @Override
	 public void registerAspect() throws Throwable {
		  Map<String,Object> beanMap = new HashMap<>();
		  beanMap = FileUtils.filterClass(new AspectReader(),beanMap);
		  for(String key:beanMap.keySet()){
				Class<?> klass = (Class) beanMap.get(key);
				Aspect aspect = klass.getAnnotation(Aspect.class);
				if(aspect == null){
					continue;
				}
				Method[] methods = klass.getDeclaredMethods();
				for (Method method:methods) {
					 Before before = method.getAnnotation(Before.class);
					 After after = method.getAnnotation(After.class);
					 Around around = method.getAnnotation(Around.class);
					 ThrowAfter trowAfter = method.getAnnotation(ThrowAfter.class);

					 if(before != null){
						 //registerBefore(before.execute(),method);
					 }
					/* if(after != null){
						  registerAfter(after.execute(),method);
					 }
					 if(around != null){
						  registerAround(around.execute(),method);
					 }
					 if(trowAfter != null){
						  registerThrowAfter(trowAfter.execute(),method);
					 }*/

				}
		  }
	 }
	 public static void main(String argsp[]) throws Throwable {
			new AOPContextImpl().registerAspect();
	 }
}
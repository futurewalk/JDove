package cn.com.dove.framework;

import cn.com.dove.anotation.Controller;

import java.util.Map;

public class AspectEntryPointReader implements IClassReader {

	 @Override
	 public <T> void read(Class<T> clazz, Map<String, Object> clazzMap) {

		  Controller controller = clazz.getAnnotation(Controller.class);
		  if (controller != null){

		  }
	 }
}
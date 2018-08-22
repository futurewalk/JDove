package cn.com.dove.framework;

import cn.com.dove.anotation.Aspect;
import cn.com.dove.anotation.Controller;

import java.util.Map;

/**
 * Created by wangdequan on 2018/3/21.
 */
public class AspectReader implements IClassReader {

   @Override
   public <T> void read(Class<T> clazz, Map<String,Object> klassMap) {
      Aspect controller = clazz.getAnnotation(Aspect.class);
      if (controller != null){
         klassMap.put(clazz.toString(),clazz);
      }
   }
}

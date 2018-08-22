package cn.com.dove.framework;

import cn.com.dove.anotation.AutoInject;
import cn.com.dove.helper.MapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangdequan on 2018/3/21.
 */
public  class AutoInjectReader implements IClassReader {
   private static final Logger logger = LoggerFactory.getLogger(AutoInjectReader.class);

   @Override
   public <T> void read(Class<T> klass, Map<String,Object> classMap) {
      Field[] fields = klass.getDeclaredFields();
      for(int i = 0; i < fields.length;i++){
         Field field = fields[i];
         AutoInject autoWrite = field.getAnnotation(AutoInject.class);
         if (autoWrite == null){
            continue;
         }
         StringBuilder keyStr = new StringBuilder(klass.toString());
         keyStr.append(".").append(field.getType().getSimpleName());
         keyStr.append(".").append(field.getName());

         MapValue mapValue = (MapValue) classMap.get(klass.toString());
         if (mapValue != null){
            Map map = mapValue.getMap();
            map.put(keyStr.toString(),field);
            mapValue.setMap(map);

            classMap.put(klass.toString(),mapValue);

         }else {
            Map map = new HashMap();
            map.put(keyStr.toString(),field);
            mapValue = new MapValue(klass,map);
            classMap.put(klass.toString(),mapValue);
         }
      }
   }
}

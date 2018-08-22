package cn.com.dove.framework;

import java.util.Map;

/**
 * Created by wangdequan on 2018/3/21.
 */
public interface IClassReader {

   <T> void read(Class<T> clazz, Map<String,Object> clazzMap) throws IllegalAccessException, InstantiationException;


}

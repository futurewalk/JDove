package cn.com.dove.framework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class RootClassReader implements IClassReader {
    private static final Logger logger = LoggerFactory.getLogger(RootClassReader.class);

    @Override
    public <T> void read(Class<T> klass, Map<String,Object> klassMap) {
        if(!klass.isInterface() && !klass.isAnnotation() && !klass.isEnum()){
            klassMap.put(klass.toString(),klass);
        }
    }
}
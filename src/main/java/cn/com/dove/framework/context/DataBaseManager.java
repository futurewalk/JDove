package cn.com.dove.framework.context;

import java.lang.reflect.Field;

public interface DataBaseManager  extends FrameworkEnvironment{

    void registerDataBase() throws Exception;

    void injectOrm(String key,Class clazz, Field field);
}
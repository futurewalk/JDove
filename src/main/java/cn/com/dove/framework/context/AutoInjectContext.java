package cn.com.dove.framework.context;

import cn.com.dove.anotation.AutoInject;
import cn.com.dove.helper.AutoInjectHelper;
import cn.com.dove.helper.MapValue;

import java.lang.reflect.Field;
import java.util.Map;

public interface AutoInjectContext extends FrameworkEnvironment {

    void registerAutoInject() throws Throwable;


    void filterAutoInject(Class implKls, Object object,MapValue mapValue) throws Exception;

    /**
     * store autoInject class to a container
     * @param field
     * @param klass
     * @param implKls
     * @param autoWrite
     * @param object
     */
    void storeAutoInject(Field field, Class klass, Class implKls, AutoInject autoWrite, Object object) throws Exception;

    /**
     * begin inject bean to the field where it set AutoInject anotation
     * @param injectKey
     * @param implMap
     * @param object
     * @throws Exception
     */
    void doInject(String injectKey, Map<String,AutoInjectHelper> implMap, Object object) throws Exception;

    /**
     * find a autoInjectBean by a controller class
     * @param object
     * @throws Exception
     */
    void autoInjectBean(Object object) throws Exception;
}
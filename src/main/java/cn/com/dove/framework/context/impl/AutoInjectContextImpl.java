package cn.com.dove.framework.context.impl;

import cn.com.dove.anotation.AutoInject;
import cn.com.dove.exception.AutowriteException;
import cn.com.dove.framework.ApplicationFactory;
import cn.com.dove.framework.AutoInjectReader;
import cn.com.dove.framework.RootClassReader;
import cn.com.dove.framework.context.AutoInjectContext;
import cn.com.dove.framework.orm.OrmHelper;
import cn.com.dove.framework.orm.Ormer;
import cn.com.dove.framework.orm.SessionFactory;
import cn.com.dove.helper.AutoInjectHelper;
import cn.com.dove.helper.MapValue;
import cn.com.dove.utils.FileUtils;
import cn.com.dove.utils.ReflectUtils;
import cn.com.dove.utils.StringTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class AutoInjectContextImpl extends DataBaseManagerImpl implements AutoInjectContext {
    private static final Logger logger = LoggerFactory.getLogger(AutoInjectContextImpl.class);
    private static final String ORM_PATH = "class cn.com.dove.framework.orm.Ormer";
    private static final String ORM_PATH_INTERFACE = "interface cn.com.dove.framework.orm.Api.Orm";
    private static Map<String, AutoInjectHelper> autoInjectImplMap = ApplicationFactory.getAutoInjectImplMap();

    @Override
    public void registerAutoInject() throws Throwable {
        Map<String,Object> autoInjectMap = FileUtils.filterClass(
                new AutoInjectReader(), ApplicationFactory.getAutoInjectMapValue());

        Map<String,Object> implMap = FileUtils.filterClass(new RootClassReader(),new HashMap());

        for(String key:autoInjectMap.keySet()){
            MapValue mapValue = (MapValue) autoInjectMap.get(key);
            Class klass = mapValue.getValue();
            Object object = klass.newInstance();
            for (String impKey:implMap.keySet()){
                Class implKls = (Class) implMap.get(impKey);
                filterAutoInject(implKls,object,mapValue);
            }
        }
   /*     for(String key:autoInjectImplMap.keySet()){
            AutoInjectHelper autoInjectHelper = autoInjectImplMap.get(key);
            //logger.info("获取当前的帮助类:{}",autoInjectHelper);
        }*/
    }

    @Override
    public void filterAutoInject(Class implKls, Object object,MapValue mapValue) throws Exception {
        Class clazz = object.getClass();
        Map<String,Object> fieldMap = mapValue.getMap();
        for(String key:fieldMap.keySet()){
            Field field = (Field) fieldMap.get(key);
            AutoInject autoWrite = field.getAnnotation(AutoInject.class);
            if(autoWrite == null){
                continue;
            }
            if(ORM_PATH.equals(field.getType().toString()) || ORM_PATH_INTERFACE.equals(field.getType().toString())){
                StringBuilder keyStr = new StringBuilder(clazz.toString());
                keyStr.append(".").append(field.getType().getSimpleName()).append(".").append(field.getName());
                if(autoInjectImplMap.get(keyStr.toString()) == null){
                    super.injectOrm(keyStr.toString(),clazz,field);
                }
                continue;
            }
            if (!field.getType().isAssignableFrom(implKls)){//如果不是该接口的实现类，则跳过
                continue;
            }
            this.storeAutoInject(field,clazz,implKls,autoWrite,object);
        }
    }
    @Override
    public void storeAutoInject(Field field, Class clazz, Class implKls, AutoInject autoWrite, Object object) throws Exception {

        StringBuilder keyStr = new StringBuilder(clazz.toString());
        keyStr.append(".").append(field.getType().getSimpleName()).append(".").append(field.getName());

        String value  = null;

        AutoInjectHelper helper  = autoInjectImplMap.get(keyStr.toString());

        if(helper != null){
            value = helper.getAutoWriteBean().toString();
        }

        if(implKls.toString().equals(value)){
            StringBuilder exStr = new StringBuilder();
            exStr.append("bean ").append(field.getType().getSimpleName())
                    .append(" inject repeat in ").append(clazz);

            throw new AutowriteException(exStr.toString());
        }
        if(StringTool.isNotNull(autoWrite.bean())){

            if(implKls.getSimpleName().equals(autoWrite.bean())){

                Map<String,Field> fieldMap = new HashMap<>();
                fieldMap.put(field.getName(),field);
                AutoInjectHelper autoInjectHelper = new AutoInjectHelper(fieldMap,implKls);
                autoInjectImplMap.put(keyStr.toString(),autoInjectHelper);
                if(!ORM_PATH.equals(implKls.toString())){
                    OrmHelper.setValue(object,implKls.newInstance(),field.getName());
                }
            }

        }else {
//            Object obj  = ReflectUtils.getFiledValue(object,field.getName());
            field.setAccessible(true);
            Object obj  = field.get(object);
            StringBuilder exStr = new StringBuilder();
            if(obj != null && !implKls.toString().equals(obj.getClass())){
                exStr.append("Multiple implementation classes exis,you should set one on this interface in ");
                exStr.append(clazz.getSimpleName()).append(".").append(field.getType().getSimpleName()).append(".").append(field.getName());

                throw new AutowriteException(exStr.toString());
            }
            Map<String,Field> fieldMap = new HashMap<>();
            fieldMap.put(field.getName(),field);
            AutoInjectHelper autoInjectHelper = new AutoInjectHelper(fieldMap,implKls);
            if(!ORM_PATH.equals(implKls.toString())){
                OrmHelper.setValue(object,implKls.newInstance(),field.getName());
            }
            autoInjectImplMap.put(keyStr.toString(),autoInjectHelper);
        }
    }

    @Override
    public void doInject(String injectKey,Map<String,AutoInjectHelper> implMap,Object object) throws Exception {

        Map<String,Object> autoInjectContainer = ApplicationFactory.getAutoInjectMapValue();
        MapValue mapValue = (MapValue) autoInjectContainer.get(injectKey);

        if(mapValue == null){
            return;
        }
        Map<String,Object> map = mapValue.getMap();
        for(String key: map.keySet()){
            Field field = (Field) map.get(key);
            AutoInjectHelper helper = implMap.get(key);
            if(helper == null){
                throw new AutowriteException(injectKey + " AutoInject failed,please check");
            }
            if(helper.getAutoWriteBean() == null){
                throw new AutowriteException(injectKey + " AutoInject failed,please check");
            }
            if(ORM_PATH.equals(String.valueOf(helper.getAutoWriteBean().toString()))){
                OrmHelper.setValue(object, SessionFactory.newOrm(),field.getName());
            }else {
                Object implObject = helper.getAutoWriteBean().newInstance();
                OrmHelper.setValue(object,implObject,field.getName());
                doInject(helper.getAutoWriteBean().toString(),implMap,implObject);
            }
        }
    }

    @Override
    public void autoInjectBean(Object object) throws Exception {
        Class clazz = object.getClass();
        Map<String,AutoInjectHelper> implMap = ApplicationFactory.getAutoInjectImplMap();
        doInject(clazz.toString(),implMap,object);
    }
}
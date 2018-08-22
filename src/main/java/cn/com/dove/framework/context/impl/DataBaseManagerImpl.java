package cn.com.dove.framework.context.impl;

import cn.com.dove.framework.ApplicationFactory;
import cn.com.dove.framework.context.DataBaseManager;
import cn.com.dove.framework.orm.DataSourceManager;
import cn.com.dove.framework.orm.Ormer;
import cn.com.dove.helper.AutoInjectHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class DataBaseManagerImpl extends FrameworkEnvironmentImpl implements DataBaseManager {
    private static final Logger logger = LoggerFactory.getLogger(DataBaseManagerImpl.class);
    private static Map<String, AutoInjectHelper> autoInjectMap = ApplicationFactory.getAutoInjectImplMap();
    @Override
    public void registerDataBase() throws Exception {
        File file = new File(Thread.currentThread().getContextClassLoader().getResource("orm.xml").getPath());
        DataSourceManager.start(file);
    }

    @Override
    public void injectOrm(String key,Class clazz, Field field) {
        Map<String,Field> fieldMap = new HashMap<>();
        fieldMap.put(field.getName(),field);
        AutoInjectHelper autoInjectHelper = new AutoInjectHelper(fieldMap, Ormer.class);
        autoInjectMap.put(key.toString(),autoInjectHelper);

    }
    public static void main(String argsp[]){
        System.out.println(Ormer.class);
     }
}
package cn.com.dove.framework;

import cn.com.dove.framework.context.ApplicationContext;
import cn.com.dove.helper.AutoInjectHelper;
import cn.com.dove.helper.RequestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;

public class ApplicationFactory {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationFactory.class);

    private static Map<String,RequestInfo> holerMap = new HashMap<>();
    private static Map<String,AutoInjectHelper> autoInjectImplMap =  new HashMap<>();
    private static Map<String,Object> autoInjectMapValue = new HashMap<>();
    private static Map<String,Object> aspectContainer = new HashMap<>();
    private static Map<String,Object> STATE_MAP = new HashMap<>();


    private static ApplicationContext applicationContext;


    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static void setApplicationContext(ApplicationContext applicationContext) {
        ApplicationFactory.applicationContext = applicationContext;
    }

    public static Map<String, RequestInfo> getHolerMap() {
        return holerMap;
    }

    public static void setHolerMap(Map<String, RequestInfo> holerMap) {
        ApplicationFactory.holerMap = holerMap;
    }

    public static void setAutoInjectImplMap(Map<String, AutoInjectHelper> autoInjectImplMap) {
        ApplicationFactory.autoInjectImplMap = autoInjectImplMap;
    }

    public static void setAutoInjectMapValue(Map<String, Object> autoInjectMapValue) {
        ApplicationFactory.autoInjectMapValue = autoInjectMapValue;
    }

    public static Map<String, Object> getAspectContainer() {
        return aspectContainer;
    }

    public static void setAspectContainer(Map<String, Object> aspectContainer) {
        ApplicationFactory.aspectContainer = aspectContainer;
    }

    public static Map<String, AutoInjectHelper> getAutoInjectImplMap() {
        return autoInjectImplMap;
    }

    public static Map<String, Object> getAutoInjectMapValue() {
        return autoInjectMapValue;
    }

    public static Map<String, Object> getStateMap() {
        return STATE_MAP;
    }

    public static void setStateMap(Map<String, Object> stateMap) {
        STATE_MAP = stateMap;
    }
}
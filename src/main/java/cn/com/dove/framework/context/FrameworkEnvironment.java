package cn.com.dove.framework.context;

import cn.com.dove.exception.HolderException;
import cn.com.dove.helper.RequestInfo;
import javassist.NotFoundException;

import java.util.Map;

public interface FrameworkEnvironment {

    /**
     * get a class by path
     * @param path
     * @return
     */
    Class getBean(String path);

    /**
     * return a controller container
     * @return
     */
    Map<String,RequestInfo> controllerMap();

    /**
     *
     * @return
     */
    RequestInfo getHolder(String path);

    /**
     * query all controllers
     * @param path
     * @throws HolderException
     */
    void registerAppContext(String path) throws Throwable;

    /**
     * store holder to map
     * @param klass
     * @param rooPath
     * @param <T>
     */
    <T> void registerHolder(Class<T> klass,String rooPath) throws HolderException, NotFoundException;

    /**
     * 初始化路由
     */
    void initRouter();

    /**
     * state,while app create before ,you can do sth you want to do
     */
    void state() throws Throwable;

    /**
     * while app create before we will do invoke this method
     */
    void beforeCreateInvoke() throws Throwable;

    /**
     * while app create after we will invoke this method
     */
    void afterCreateInvoke() throws IllegalAccessException, InstantiationException;
}
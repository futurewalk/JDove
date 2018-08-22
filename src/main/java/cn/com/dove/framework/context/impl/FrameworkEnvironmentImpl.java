package cn.com.dove.framework.context.impl;

import cn.com.dove.anotation.Controller;
import cn.com.dove.anotation.ResMapping;
import cn.com.dove.exception.HolderException;
import cn.com.dove.framework.*;
import cn.com.dove.framework.context.FrameworkEnvironment;
import cn.com.dove.helper.FileHelper;
import cn.com.dove.helper.RequestInfo;
import cn.com.dove.utils.FileUtils;
import cn.com.dove.utils.StateManager;
import cn.com.dove.utils.StringTool;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class FrameworkEnvironmentImpl implements FrameworkEnvironment {
    private static final Logger logger = LoggerFactory.getLogger(FrameworkEnvironmentImpl.class);
    private static final String regEx = ".*\\{*\\}";
    private static final String regExDot = ".*\\/:.*";

    @Override
    public Class getBean(String path) {
        return controllerMap().get(path).getClazz();
    }

    @Override
    public Map<String, RequestInfo> controllerMap() {
        return ApplicationFactory.getHolerMap();
    }

    @Override
    public RequestInfo getHolder(String path) {
        return controllerMap().get(path);
    }

    @Override
    public void registerAppContext(String packageName) throws Throwable {
        Map<String, Object> beanMap = new HashMap<>();

        if (!StringTool.isNotNull(packageName)) {
            beanMap = FileUtils.filterClass(new ControllerReader(), beanMap);
        } else {
            beanMap = FileUtils.getClasses(packageName, new ControllerReader(), beanMap);
        }

        for (String key : beanMap.keySet()) {
            Class clazz = (Class) beanMap.get(key);
            Controller controller = (Controller) clazz.getAnnotation(Controller.class);

            if (controller != null) {
                registerHolder(clazz, controller.Router());
            }
        }
    }

    @Override
    public <T> void registerHolder(Class<T> clazz, String rooPath) throws HolderException, NotFoundException {
        Method[] methods = clazz.getMethods();

        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            ResMapping resMapping = method.getAnnotation(ResMapping.class);

            if (resMapping == null) continue;
            String url = resMapping.url();
            if (!StringTool.isNotNull(url)) continue;

            if (Modifier.isStatic(method.getModifiers())) {
                throw new HolderException(clazz.getSimpleName() + "." + method.getName() + " must be not a static method ");
            }

            Class<?>[] argsTypes = method.getParameterTypes();
            String[] paramNames = getMethodVariableName(clazz.getName(), method.getName(),argsTypes.length);

            String restFulKey = null;

            if (resMapping.url().matches(regEx)) {

                restFulKey = url.substring(url.indexOf("{") + 1, url.lastIndexOf("}"));
                url = url.substring(0, url.lastIndexOf("/") + 1);
            } else if (resMapping.url().matches(regExDot)) {

                restFulKey = url.substring(url.indexOf(":") + 1, url.length());
                url = url.substring(0, url.lastIndexOf("/") + 1);
            }

            RequestInfo requestInfo = new RequestInfo(clazz, method.getName(),
                    rooPath + url, paramNames, argsTypes,
                    restFulKey, resMapping.action().name());

            ApplicationFactory.getHolerMap().put("/" + rooPath + url, requestInfo);

            logger.info("router create success: {}", new Object[]{"/" + rooPath + url});
        }
    }

    public static String[] getMethodVariableName(String clazzName, String methodName,int paramLength) throws NotFoundException {
        ClassPool pool = ClassPool.getDefault();
        pool.insertClassPath(Thread.currentThread().getContextClassLoader().getResource("/").getPath());
        CtClass cc = pool.get(clazzName);
        CtMethod cm = cc.getDeclaredMethod(methodName);
        MethodInfo methodInfo = cm.getMethodInfo();
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
        String[] paramNames = new String[paramLength];
        LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
        if (attr != null) {
            int pos = javassist.Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
            for (int i = 0; i < paramNames.length; i++) {
                paramNames[i] = attr.variableName(i + pos);
            }
            return paramNames;
        }
        return null;
    }

    @Override
    public void initRouter() {
        Map<String, Object> routerMap = new HashMap();
        IClassReader classReader = new IClassReader() {
            @Override
            public <T> void read(Class<T> clazz, Map<String, Object> clazzMap) {
                if (Router.class.isAssignableFrom(clazz.getClass())) {

                }
            }
        };
    }

    @Override
    public void state() throws Throwable {
        Map<String, Object> stateMap = new HashMap();
        Map<String, Object> requestStateMap = new HashMap<>();

        FileUtils.filterClass(new IClassReader() {
            @Override
            public <T> void read(Class<T> clazz, Map<String, Object> clazzMap) throws IllegalAccessException, InstantiationException {
                if (AbstractState.class.isAssignableFrom(clazz)) {
                    clazzMap.put(clazz.getName(), clazz);
                    StateManager.beforeCreate((AbstractState) clazz.newInstance());
                }
                if (AbstractRequestListener.class.isAssignableFrom(clazz)) {
                    requestStateMap.put(clazz.getName(), clazz);

                }
            }
        }, stateMap);
        stateMap.put(AbstractRequestListener.class.getSimpleName(), requestStateMap);
        ApplicationFactory.setStateMap(stateMap);
    }

    @Override
    public void beforeCreateInvoke() throws Throwable {
        this.state();
    }

    @Override
    public void afterCreateInvoke() throws IllegalAccessException, InstantiationException {
        Map<String, Object> stateMap = ApplicationFactory.getStateMap();
        for (String key : stateMap.keySet()) {
            Object object = stateMap.get(key);
            if (object instanceof HashMap) {
                continue;
            }
            Class clazz = (Class) stateMap.get(key);
            if (AbstractState.class.isAssignableFrom(clazz)) {
                StateManager.afterCreate((AbstractState) clazz.newInstance());
            }
        }
    }

}
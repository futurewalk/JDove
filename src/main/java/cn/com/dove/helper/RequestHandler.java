package cn.com.dove.helper;

import cn.com.dove.anotation.ResMapping;
import cn.com.dove.exception.ControllerNotFoundException;
import cn.com.dove.exception.MessageException;
import cn.com.dove.framework.ApplicationFactory;
import cn.com.dove.framework.context.ApplicationContext;
import cn.com.dove.framework.em.LoginState;
import cn.com.dove.utils.ReflectUtils;
import cn.com.dove.utils.StateManager;
import cn.com.dove.utils.StringTool;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangdequan on 2018/1/29.
 */
public class RequestHandler {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static ThreadLocal<Map<String, String>> VALUES_MAP = new ThreadLocal<>();
    private static final String WEB_CHERSET = "UTF-8";
    private static Map<String, String> REQUEST_MAP = new HashMap<>();

    /**
     * 检测是否是恶意请求
     */
    public static void checkRequest(HttpServletRequest req, HttpServletResponse resp) throws MessageException {
        String ip = req.getRemoteAddr();

        String preReqTime = REQUEST_MAP.get(ip);
        if (!StringTool.isNotNull(preReqTime)) {
            REQUEST_MAP.put(ip, String.valueOf(System.currentTimeMillis()));
            return;
        }
        if (System.currentTimeMillis() - Long.valueOf(preReqTime) < 2) {
            throw new MessageException("请求过于频繁，让程序员哥哥缓一缓!");
        }

    }

    public static Object invoke(HttpServletRequest request, HttpServletResponse response) throws Throwable {

        ApplicationContext context = ApplicationFactory.getApplicationContext();
        String path = realRequestUrl(request);

        if (path == null) {
            throw new MessageException("url is null");
        }

        Map<String, RequestInfo> holderMap = ApplicationFactory.getHolerMap();

        if (restFul(holderMap, path)) {
            path = path.substring(0, path.lastIndexOf("/") + 1);
        }

        RequestInfo requestInfo = holderMap.get(path);

        if (requestInfo == null) {
            throw new ControllerNotFoundException("not found controller mapping");
        }

        Class clazz = requestInfo.getClazz();
        Object object = clazz.newInstance();
        context.autoInjectBean(object);

        Method method = clazz.getMethod(requestInfo.getMethod(), requestInfo.getParamTypes());
        Object obj = methodInvoke(object, method, requestInfo, holderMap,request,response);

        logger.info("resp data:{}", JSON.toJSON(RespUtils.success(obj)));

        return obj;
    }
    private static String realRequestUrl(HttpServletRequest request){
        String path = request.getRequestURI();
        String servletUrl = request.getServletPath();
        return path.replace(servletUrl,"");
    }

    private static Object methodInvoke(Object object, Method method, RequestInfo requestInfo,
            Map<String, RequestInfo> holderMap,HttpServletRequest request,HttpServletResponse response)
            throws Throwable {
        Class<?>[] argsTypes = requestInfo.getParamTypes();
        ResMapping resMapping = method.getAnnotation(ResMapping.class);
        LoginState state = resMapping.loginState();

        StateManager.checkLoginState(state,request);

        FileUpload fileUpload = null;
        if (resMapping != null && resMapping.uploadFile()) {
            fileUpload = new FileUpload();
            fileUpload.setRequest(request);

        }
        Object[] argsValues = null;
        if (argsTypes.length > 0) {
            argsValues = new Object[argsTypes.length];
        }
        String[] arguments = requestInfo.getArguments();
        Map<String, String> map = new HashMap<>();
        String url = realRequestUrl(request);

        if (restFul(holderMap, url)) {
            String value = null;
            if (url.lastIndexOf("\\/:") > -1) {
                value = url.substring(url.lastIndexOf("/:") + 1, url.length());
            } else {
                value = url.substring(url.lastIndexOf("/") + 1, url.length());
            }
            map.put(requestInfo.getRestful(), value);
        }
        boolean haveUpload = true;
        for (int i = 0; i < argsTypes.length; i++) {
            Class type = argsTypes[i];
            if (JDove.class.getName().equals(type.getName())) {
                argsValues[i] = new RequestHandler.JDoveHelpImpl(request,response);
                continue;
            }

            if (FileUpload.class.getName().equals(type.getName()) && haveUpload) {
                argsValues[i] = fileUpload;
                continue;
            }
            String value = request.getParameter(arguments[i]);
            Object retValue = ReflectUtils.castValue(StringTool.upperCase(type.getSimpleName()), value == null ? value : URLDecoder.decode(value, WEB_CHERSET));
            argsValues[i] = retValue;
            map.put(arguments[i], value);
        }
        VALUES_MAP.set(map);
        return method.invoke(object, argsValues);
    }
    private static boolean restFul(Map<String, RequestInfo> holderMap, String url) throws ControllerNotFoundException {
        if (url == null) {
            throw new ControllerNotFoundException(" controller not found");
        }
        String newUrl = url.substring(0, url.lastIndexOf("/") + 1);

        if (holderMap.get(newUrl) == null) {
            return false;
        }
        return StringTool.isNotNull(holderMap.get(newUrl).getRestful());
    }

    private static class JDoveHelpImpl implements JDove {
        private HttpServletRequest request;
        private HttpServletResponse response;
        private JDoveHelpImpl(HttpServletRequest request,HttpServletResponse response) {
            this.request = request;
            this.response = response;
        }

        @Override
        public HttpServletRequest request() {
            return request;
        }

        @Override
        public HttpServletResponse respone() {
            return response;
        }

        @Override
        public String getValue(String key) {
            String obj = request.getParameter(key);
            if (obj != null) {
                return obj;
            }
            return VALUES_MAP.get().get(key);
        }

        @Override
        public void setSession(String key, Object value) {
            request.getSession(true).setAttribute(key, value);
        }

        @Override
        public HttpSession getSession() {
            return request.getSession();
        }

        @Override
        public Object getSessionValue(String key) {
            HttpSession session = request.getSession();
            return session.getAttribute(session.getId());
        }

        @Override
        public <T> T findBean(Class<T> clazz) throws Exception {

            HttpServletRequest request = request();
            Field[] fields = clazz.getDeclaredFields();
            Object object = clazz.newInstance();
            for (Field field : fields) {
                if(!Modifier.isFinal(field.getModifiers())){
                    field.setAccessible(true);
                    String value  = request.getParameter(field.getName());
                    field.set(object, ReflectUtils.castValue(field.getType(),value));
                }
            }
            return (T) object;
        }
    }
    public static void main(String args[]){
        System.out.println(FileUpload.class.getName());
    }
}

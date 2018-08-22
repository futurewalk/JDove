package cn.com.dove.utils;

import cn.com.dove.exception.LoginStateException;
import cn.com.dove.framework.AbstractRequestListener;
import cn.com.dove.framework.AbstractState;
import cn.com.dove.framework.em.LoginState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

public class StateManager {
	private static final Logger logger = LoggerFactory.getLogger(StateManager.class);
	/**
	 * app create before listener
	 * @param state
	 */
	public static void beforeCreate(AbstractState state){
		state.beforeCreate();
	}
	/**
	 * app create finish listener
	 * @param state
	 */
	public static void afterCreate(AbstractState state){
		state.afterCreate();
	}

	/**
	 * listen request before
	 * @param listenerMap
	 */
	public static void onRequestBefore(Map<String,Object> listenerMap) {
		try{
			for(Object key:listenerMap.keySet()){
				Class clazz = (Class) listenerMap.get(key);
				AbstractRequestListener listener = (AbstractRequestListener) clazz.newInstance();
				listener.onRequestBefore();
			}
		}catch(Exception e){
			 e.printStackTrace();
		}
	}
	/**
	 * listen request error
	 * @param listenerMap
	 */
	public static void onRequestError(Map<String,Object> listenerMap){
		try{
			for(Object key:listenerMap.keySet()){
				Class clazz = (Class) listenerMap.get(key);
				AbstractRequestListener listener = (AbstractRequestListener) clazz.newInstance();
				listener.onRequestError();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * listen request finish
	 * @param listenerMap
	 */
	public static void onRequestFinish(Map<String,Object> listenerMap){
		try{
			for(Object key:listenerMap.keySet()){
				Class clazz = (Class) listenerMap.get(key);
				AbstractRequestListener listener = (AbstractRequestListener) clazz.newInstance();
				listener.onRequestFinish();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void checkLoginState(LoginState loginState, HttpServletRequest request) throws LoginStateException {

		HttpSession session  = request.getSession();
		Object value = session.getAttribute(session.getId());

		if(loginState.name() == LoginState.MUST_LOGIN.name()){
			if(value == null){
				throw new LoginStateException("you are not login");
			}
		}
		if(loginState.name() == LoginState.MUST_NOT_LOGIN.name()){
			if(value != null){
				throw new LoginStateException("no permission");
			}
		}

	}
}
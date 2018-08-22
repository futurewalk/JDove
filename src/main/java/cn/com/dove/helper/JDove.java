package cn.com.dove.helper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by wangdequan on 2018/2/5.
 */
public interface JDove {

  /**
   * return request
   * @return
   */
  HttpServletRequest request();

  /**
   * return respone
   * @return
   */
  HttpServletResponse respone();

  /**
   * get value by container ,
   * if resful param equals method param,
   * method param will Override the restful param
   * @param key
   * @return
   */
  String getValue(String key);

  /**
   * set session value
   * @param key
   * @param value
   */
  void setSession(String key, Object value);

  /**
   * getSession
   * @return
   */
  HttpSession getSession();

  /**
   * get session value by key
   * @param key
   * @return
   */
  Object getSessionValue(String key);

  /**
   * find abean by class
   * @param klass
   * @param <T>
   * @return
   */
  <T> T findBean(Class<T> klass) throws Exception;

}

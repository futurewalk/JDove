package cn.com.dove.utils;

/**
 * Created by wangdequan on 2018/2/5.
 */
public class Constans {

   public static final int SUCCESS = 200;
   public static String SUCCESS_MSG = "SUCCESS";

   /**
    * unfound the url mapping controller or method
    */
   public static final int UN_FOUND = 404;
   public static String UN_FOUND_CLASS_MSG = "Bad Request";


   /**
    * serives inner error
    */
   public static final int INNER_ERROR = 500;
   public static String INNER_ERROR_MSG = "internal server erro";

   /**
    * defer unlogin code
    */
   public static final int LOGIN_STATE = 302;
   public static String UN_LOGIN_MSG = "unlogin";

   /**
    * unknow err found
    */
   public static final int JDOVE_SELF_ERR = 503;
   public static String UNKNOW_ERROR_MSG = "Service Unavailable";
}

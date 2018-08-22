package cn.com.dove.exception;

/**
 * Created by wangdequan on 2018/2/1.
 */
public class AutowriteException extends RuntimeException {

   public AutowriteException(String msg){
      super(msg);
   }

   public AutowriteException(String message, Throwable cause) {
      super(message, cause);
   }

   public AutowriteException(Throwable cause) {
      super(cause);
   }

   public static DBAccessException newDBAccessException(String message) {
      return new DBAccessException(message);
   }

   public static DBAccessException newDBAccessException(Throwable t) {
      return new DBAccessException(t);
   }

   public static RuntimeException convert(Exception e) {
      return (RuntimeException)(e instanceof RuntimeException?(RuntimeException)e:newDBAccessException((Throwable)e));
   }
}

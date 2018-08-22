//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.com.dove.exception;

public class DBAccessException extends RuntimeException {
   private static final long serialVersionUID = -5910158079120701473L;

   public DBAccessException() {
   }

   public DBAccessException(String message) {
      super(message);
   }

   public DBAccessException(String message, Throwable cause) {
      super(message, cause);
   }

   public DBAccessException(Throwable cause) {
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

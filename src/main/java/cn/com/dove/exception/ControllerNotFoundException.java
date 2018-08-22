package cn.com.dove.exception;

/**
 * Created by wangdequan on 2018/2/1.
 */
public class ControllerNotFoundException extends Throwable {

   public ControllerNotFoundException(String msg){
      super(msg);
   }
   public static HolderException newErr(String msg){
      System.exit(0);
      return new HolderException(msg);
   }
}

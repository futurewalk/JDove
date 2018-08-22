package cn.com.dove.exception;

/**
 * Created by wangdequan on 2018/2/5.
 */
public class HolderException extends Throwable {
   public HolderException(String msg){
      super(msg);
   }
   public static HolderException newErr(String msg){
      System.exit(0);
      return new HolderException(msg);
   }
}

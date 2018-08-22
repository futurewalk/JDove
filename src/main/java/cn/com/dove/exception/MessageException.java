package cn.com.dove.exception;

/**
 * Created by wangdequan on 2018/2/1.
 */
public class MessageException extends RuntimeException {


   private static final long serialVersionUID = -102324565904130269L;
   private int code;

   public MessageException(String message) {
      super(message);
   }

   public MessageException(Throwable cause) {
      super(cause);
   }

   public int getCode() {
      return this.code;
   }

   public MessageException setCode(int code) {
      this.code = code;
      return this;
   }

   public static MessageException newMessageException(String message) {
      return newMessageException(0, message);
   }

   public static MessageException newMessageException(int code, String message) {
      return (new MessageException(message)).setCode(code);
   }

   public static MessageException newMessageException(Exception e) {
      return new MessageException(e);
   }
}

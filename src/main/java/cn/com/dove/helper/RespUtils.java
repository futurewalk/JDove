package cn.com.dove.helper;

import cn.com.dove.utils.Constans;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangdequan on 2018/2/5.
 */
public class RespUtils {

   private int status;

   private String msg;

   private long submitTime;

   private Object object;

   private RespUtils(int status, String msg, long submitTime, Object object){
      this.status = status;
      this.msg = msg;
      this.submitTime = submitTime;
      this.object = object;
   }
   public static Map success(Object object){
      return  retMap(Constans.SUCCESS,object,Constans.SUCCESS_MSG);
   }
   public static Map internalServiceError(String msg){
      return  retMap(Constans.INNER_ERROR,null,msg);
   }
   public static Map notFound(){
      return  retMap(Constans.UN_FOUND,null,Constans.UN_FOUND_CLASS_MSG);
   }
   public static Map loginState(String msg){
      return  retMap(Constans.LOGIN_STATE,null,msg);
   }

   public static Map defineResp(String msg){
      return  retMap(Constans.JDOVE_SELF_ERR,new Object[]{},msg);
   }

   private static Map retMap(int status,Object object,String msg){
      Map<String,Object> retMap = new HashMap<>();
      retMap.put("status",status);
      retMap.put("msg",msg);
      retMap.put("submitTime",System.currentTimeMillis());
      retMap.put("result",object);
      return  retMap;
   }


   public int getStatus() {
      return status;
   }

   public void setStatus(int status) {
      this.status = status;
   }

   public String getMsg() {
      return msg;
   }

   public void setMsg(String msg) {
      this.msg = msg;
   }

   public long getSubmitTime() {
      return submitTime;
   }

   public void setSubmitTime(long submitTime) {
      this.submitTime = submitTime;
   }

   public Object getObject() {
      return object;
   }

   public void setObject(Object object) {
      this.object = object;
   }
}

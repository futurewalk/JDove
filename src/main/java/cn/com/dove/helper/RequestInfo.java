package cn.com.dove.helper;

import com.alibaba.fastjson.JSON;

import java.io.File;
import java.lang.reflect.Field;

/**
 * Created by wangdequan on 2018/1/29.
 */
public class RequestInfo {

   private String method;

   private String path;
   private Class clazz;
   private String restful;
   private String reqType;
   private String arguments[];
   private Class paramTypes[];

   public RequestInfo(){
   }

   public RequestInfo(Class clazz, String method, String path, String[] arguments,
							 Class[] paramTypes, String restful, String reqType){
      this.clazz = clazz;
      this.method = method;
      this.path = path;
      this.arguments = arguments;
      this.paramTypes = paramTypes;
      this.restful = restful;
      this.reqType = reqType;
   }

   public String getMethod() {
      return method;
   }

   public void setMethod(String method) {
      this.method = method;
   }

   public String getPath() {
      return path;
   }

   public void setPath(String path) {
      this.path = path;
   }

   public Class getClazz() {
      return clazz;
   }

   public void setClazz(Class clazz) {
      this.clazz = clazz;
   }

   public String[] getArguments() {
      return arguments;
   }

   public void setArguments(String[] arguments) {
      this.arguments = arguments;
   }

   public Class[] getParamTypes() {
      return paramTypes;
   }

   public void setParamTypes(Class[] paramTypes) {
      this.paramTypes = paramTypes;
   }

   public String getRestful() {
      return restful;
   }

   public void setRestful(String restful) {
      this.restful = restful;
   }

   public String getReqType() {
      return reqType;
   }

   public void setReqType(String reqType) {
      this.reqType = reqType;
   }

   public String toString(){
      return JSON.toJSONString(this);
   }

}

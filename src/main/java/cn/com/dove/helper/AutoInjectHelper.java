package cn.com.dove.helper;

import com.alibaba.fastjson.JSON;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by wangdequan on 2018/3/22.
 */
public class AutoInjectHelper {

   private Class autoWriteBean;
   private Map<String,Field> fieldMap;

   public AutoInjectHelper(Map<String,Field> fieldMap, Class autoWriteBean){
      this.fieldMap = fieldMap;
      this.autoWriteBean = autoWriteBean;
   }

   public Class getAutoWriteBean() {
      return autoWriteBean;
   }

   public void setAutoWriteBean(Class autoWriteBean) {
      this.autoWriteBean = autoWriteBean;
   }


   public Map<String, Field> getFieldMap() {
      return fieldMap;
   }

   public void setFieldMap(Map<String, Field> fieldMap) {
      this.fieldMap = fieldMap;
   }

   @Override
   public String toString() {
      return JSON.toJSONString(this);
   }
}

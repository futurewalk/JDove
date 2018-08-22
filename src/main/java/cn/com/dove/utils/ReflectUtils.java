package cn.com.dove.utils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by wangdequan on 2018/2/5.
 */
public class ReflectUtils {
   public static <T> T castValue(String fieldType, String value){
      T ctValue  = null;

      if (BaseType.INT_STR.equals(fieldType) || BaseType.INTEGER_STR.equals(fieldType)) {
         ctValue = value==null ? (T) Integer.valueOf(0):(T) Integer.valueOf(value);
      } else if (BaseType.STRING_STR.equals(fieldType)) {

         ctValue = value==null ? (T) "" : (T)value;
      } else if (BaseType.BIGDECIMAL_STR.equals(fieldType)) {

         ctValue = value==null?(T) BigDecimal.ZERO:(T)new BigDecimal(value);

      } else if (BaseType.FLOAT_STR.equals(fieldType)) {

         ctValue = value==null?(T) Float.valueOf(0):(T) Float.valueOf(value);

      } else if (BaseType.DOUBLE_STR.equals(fieldType)) {

         ctValue = value==null?(T)Double.valueOf(0.00):(T) Float.valueOf(value);

      } else if (BaseType.TIMESTAMP_STR.equals(fieldType)) {

         ctValue = value==null?(T) Timestamp.valueOf(0+""):(T)Timestamp.valueOf(value);

      } else if (BaseType.LONG_STR.equals(fieldType)) {
         ctValue = value==null?(T)Long.valueOf(0L):(T)Long.valueOf(value);
      } else if (BaseType.BOOLEAN_STR.equals(fieldType)) {

         ctValue = (T) Long.valueOf(value);
      }
      return ctValue;
   }
   /**
    * create getMethod by field
    * @param field
    * @return
    */
   public static String createGetMethod(Object object,String field) throws NoSuchFieldException {
      return createGetMethod(object.getClass(),field);
   }

   public static String createGetMethod(Class klass,String field) throws NoSuchFieldException {
      char[] chars = field.toCharArray();

      Field fd = klass.getDeclaredField(field);

      StringBuilder sb = new StringBuilder();

      if (BaseType.BOOLEAN_STR.equals(StringTool.upperCase(fd.getType().getSimpleName()))){
         sb.append("is");
      }else{
         sb.append("get");
      }

      for (int i = 0;i < chars.length;i++){
         if (i==0){
            sb.append(Character.toUpperCase(chars[i]));
            continue;
         }
         sb.append(chars[i]);
      }
      return sb.toString();
   }
   public static Object getFiledValue(Object object,String field) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException, InstantiationException {
      Method method = object.getClass().getMethod(createGetMethod(object.getClass(),field));
      return method.invoke(object);
   }

   /**
    * find bean by request
    * @param clazz
    * @param request
    * @param <T>
    * @return
    */
   public static <T> T findBean(Class<T> clazz, HttpServletRequest request) throws Exception {
      Field[] fields = clazz.getDeclaredFields();
      Object object = clazz.newInstance();
      for(Field field:fields){
         field.setAccessible(true);
         field.set(object,castValue(field.getType(),request.getParameter(field.getName())));
      }

      return (T) object;
   }
   public static <T> T map2Bean(Map<String, Object> map, Class<T> clas) {
      T klass = null;
      try {
         klass = clas.newInstance();
         for (Map.Entry<String, Object> obj : map.entrySet()) {
            String key = obj.getKey();
            Object val = obj.getValue();
            Field fields = getDeclaredField(klass, key);
            Class type = fields.getType();
            fields.setAccessible(true);
            fields.set(klass, castValue(type, val));
         }
      } catch (IllegalAccessException e) {
         e.printStackTrace();
      } catch (InstantiationException e) {
         e.printStackTrace();
      } catch (Exception e) {
         e.printStackTrace();
      }
      return klass;
   }

   public static <T> T castValue(Class<T> type, Object value) throws Exception {
      String fieldType = upperCase(type.getSimpleName());
      T val = null;
      if (BaseType.INT_STR.equals(fieldType) || BaseType.INTEGER_STR.equals(fieldType)) {
         value = value == null?0L:value;
         if(!StringTool.isNotNull(String.valueOf(value))){
            value = 0;
         }
         val = (T) Integer.valueOf(String.valueOf(value));
      }

      if (BaseType.STRING_STR.equals(fieldType)) {
         val = (T) String.valueOf(value);
      }

      if (BaseType.BIGDECIMAL_STR.equals(fieldType)) {
         if(!StringTool.isNotNull(String.valueOf(value))){
            value = 0;
         }
         val = (T) new BigDecimal(String.valueOf(value));

      }

      if (BaseType.FLOAT_STR.equals(fieldType)) {
         if(!StringTool.isNotNull(String.valueOf(value))){
            value = 0.0f;
         }
         val = (T) Float.valueOf(String.valueOf(value));
      }

      if (BaseType.DOUBLE_STR.equals(fieldType)) {
         if(!StringTool.isNotNull(String.valueOf(value))){
            value = 0.0d;
         }
         val = (T) Double.valueOf(String.valueOf(value));

      }

      if (BaseType.TIMESTAMP_STR.equals(fieldType)) {
         val = (T) Timestamp.valueOf(String.valueOf(value));

      }

      if (BaseType.LONG_STR.equals(fieldType)) {
         if(!StringTool.isNotNull(String.valueOf(value))){
            value = 0L;
         }
         val = (T) Long.valueOf(String.valueOf(value));
      }

      if (BaseType.BOOLEAN_STR.equals(fieldType)) {
         val = (T) Boolean.valueOf(String.valueOf(value));
      }

      return val;
   }
   private static String upperCase(String word) throws Exception {
      if (!StringTool.isNotNull(word)) {
         return null;
      }
      char[] cword = word.toCharArray();

      StringBuilder sb = new StringBuilder();

      for (Character wd : cword) {
         sb.append(Character.toUpperCase(wd));
      }
      return sb.toString();
   }

   public static Field getDeclaredField(Object object, String filedName) {

      for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
         try {
            return superClass.getDeclaredField(filedName);
         } catch (NoSuchFieldException e) {
            e.printStackTrace();
         }
      }
      return null;
   }


}

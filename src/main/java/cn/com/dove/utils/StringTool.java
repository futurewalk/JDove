package cn.com.dove.utils;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wangdequan on 2018/2/1.
 */
public class StringTool {
   private static final String characterStr = "qwertyuiopasdfghjklzxcvbnm0123456789";
   private static final String format = "yyyyMMddHHmmss";


   public static String uuId(){
      char[] chars = characterStr.toCharArray();
      StringBuilder sb = new StringBuilder();

      try{
         synchronized (Object.class){
            sb.append(DateTool.getString(new Date(),format));
            for(int i = 0;i < 22;i++){
               int index = (int) (Math.random()*22);
               sb.append(chars[index]);
            }
         }
      }catch(Exception e){
         e.printStackTrace();
      }

      return sb.toString();
   }
   public static boolean isNotNull(String ...values){
      if(values==null){
         return false;
      }
      for(String v :values){
         if("".equals(v)||"null".equals(v)||v==null||"NULL".equals(v.trim())){
            return false;
         }
      }
      return true;
   }
   public static boolean equals(String value1,String value2){
      if(value1 == null && value1 ==null){
         return true;
      }
      if(value1 == null && value1 != null){
         return false;
      }
      if(value2 == null && value1 !=null){
         return false;
      }
      if(value1.equals(value2)){
         return true;
      }
      return false;
   }
   public static String upperCase(String word){
      char[] chars = word.toCharArray();

      StringBuilder sb = new StringBuilder();

      for(int i = 0;i < chars.length;i++){
         sb.append(Character.toUpperCase(chars[i]));
      }
      return sb.toString();
   }
   public static String lowerCase(String word){
      char[] chars = word.toCharArray();
      StringBuilder sb = new StringBuilder();
      for(int i = 0;i < chars.length;i++){
         sb.append(Character.toLowerCase(chars[i]));
      }
      return sb.toString();
   }
   public static String[] split(String url, String string) {
      return url.split(string) ;
   }

   public static String trim(String input)
   {
      return input==null ? null : input.trim() ;
   }


   public static boolean isNotEmpty(String input) {
      return input!=null && !input.trim().isEmpty()&& !"null".equals(input) ;
   }
   public static boolean isEmpty(String input) {
      return input==null || input.trim().isEmpty() ;
   }

   /**
    * 过滤SQL字符串,防止SQL inject
    * @param sql
    * @return String
    */
   public static String encodeSQL(String sql)
   {
      if (sql == null)
      {
         return "";
      }
      // 不用正则表达式替换，直接通过循环，节省cpu时间
      StringBuilder sb = new StringBuilder();
      for(int i = 0; i < sql.length(); ++i)
      {
         char c = sql.charAt(i);
         switch(c)
         {
            case '\\':
               sb.append("\\\\");
               break;
            case '\r':
               sb.append("\\r");
               break;
            case '\n':
               sb.append("\\n");
               break;
            case '\t':
               sb.append("\\t");
               break;
            case '\b':
               sb.append("\\b");
               break;
            case '\'':
               sb.append("\'\'");
               break;
            case '\"':
               sb.append("\\\"");
               break;
            default:
               sb.append(c);
         }
      }
      return sb.toString();
   }





   /**
    * 转换&#123;这种编码为正常字符<br/>
    * 有些手机会将中文转换成&#123;这种编码,这个函数主要用来转换成正常字符.
    * @param str
    * @return String
    */
   public static String decodeNetUnicode(String str)
   {
      if(str == null)
         return null;

      String pStr = "&#(\\d+);";
      Pattern p = Pattern.compile(pStr);
      Matcher m = p.matcher(str);
      StringBuffer sb = new StringBuffer();
      while (m.find()) {
         String mcStr = m.group(1);
         int charValue = convertInt(mcStr, -1);
         String s = charValue > 0 ? (char) charValue + "" : "";
         m.appendReplacement(sb, Matcher.quoteReplacement(s));
      }
      m.appendTail(sb);
      return sb.toString();
   }

   /**
    * 获取字符型参数，若输入字符串为null，则返回设定的默认值
    * @param str 输入字符串
    * @param defaults 默认值
    * @return 字符串参数
    */
   public static String convertString(String str, String defaults)
   {
      if(str == null)
      {
         return defaults;
      }
      else
      {
         return str;
      }
   }


   /**
    *
    * @param input
    * @param def
    * @return
    */
   public static int convertInt(String input, int def) {
      try
      {
         return Integer.valueOf(input) ;
      } catch (Exception e) {}
      return def ;
   }

   /**
    * 获取long型参数，若输入字符串为null或不能转为long，则返回设定的默认值
    * @param str 输入字符串
    * @param defaults 默认值
    * @return long参数
    */
   public static long convertLong(String str, long defaults)
   {
      if(str == null)
      {
         return defaults;
      }
      try
      {
         return Long.parseLong(str);
      }
      catch(Exception e)
      {
         return defaults;
      }
   }

   /**
    * 获取double型参数，若输入字符串为null或不能转为double，则返回设定的默认值
    * @param str 输入字符串
    * @param defaults 默认值
    * @return double型参数
    */
   public static double convertDouble(String str, double defaults)
   {
      if(str == null)
      {
         return defaults;
      }
      try
      {
         return Double.parseDouble(str);
      }
      catch(Exception e)
      {
         return defaults;
      }
   }

   /**
    * 获取short型参数，若输入字符串为null或不能转为short，则返回设定的默认值
    * @param str 输入字符串
    * @param defaults 默认值
    * @return short型参数
    */
   public static short convertShort(String str, short defaults)
   {
      if(str == null)
      {
         return defaults;
      }
      try
      {
         return Short.parseShort(str);
      }
      catch(Exception e)
      {
         return defaults;
      }
   }

   /**
    * 获取float型参数，若输入字符串为null或不能转为float，则返回设定的默认值
    * @param str 输入字符串
    * @param defaults 默认值
    * @return float型参数
    */
   public static float convertFloat(String str, float defaults)
   {
      if(str == null)
      {
         return defaults;
      }
      try
      {
         return Float.parseFloat(str);
      }
      catch(Exception e)
      {
         return defaults;
      }
   }

   /**
    * 获取boolean型参数，若输入字符串为null或不能转为boolean，则返回设定的默认值
    * @param str 输入字符串
    * @param defaults 默认值
    * @return boolean型参数
    */
   public static boolean convertBoolean(String str, boolean defaults)
   {
      if(str == null)
      {
         return defaults;
      }
      try
      {
         return Boolean.parseBoolean(str);
      }
      catch(Exception e)
      {
         return defaults;
      }
   }

   public static String listToString(List<?> list, String sep) {
      if (list == null) {
         return null;
      }
      StringBuilder result = new StringBuilder();
      boolean flag = false;
      for (Object o : list) {
         if (flag) {
            result.append(sep==null?",":sep);
         } else {
            flag = true;
         }
         result.append(o.toString());
      }
      return result.toString();
   }
    public static void main(String argsp[]){
    }

}

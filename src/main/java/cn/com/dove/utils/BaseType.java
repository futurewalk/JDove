package cn.com.dove.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by wangdequan on 2018/1/11.
 */
public class BaseType {
   public static String INT_STR = "INT";
   public static String INTEGER_STR = "INTEGER";
   public static String STRING_STR = "STRING";
   public static String DOUBLE_STR = "DOUBLE";
   public static String BIGDECIMAL_STR = "BIGDECIMAL";
   public static String FLOAT_STR = "FLOAT";
   public static String BOOLEAN_STR = "BOOLEAN";
   public static String BYT_STR = "BYT";
   public static String SHORT_STR = "SHORT";
   public static String LONG_STR = "LONG";
   public static String TIMESTAMP_STR = "TIMESTAMP";

   public static void main(String argsp[]){
      List list = new ArrayList();
      for (int i =0;i< 130;i++){
         list.add(i);
      }
      Iterator it = list.iterator();
  /*    while (it.hasNext()){
         System.out.println(it.next());
         System.out.println("=====>"+it.next());
      }
      System.out.println("获取数据集合:"+list.get(4));*/
      boolean v = true;
      int i = 0;
      for (;it.hasNext();i++){
         System.out.println(it.next()+"========>"+i);
      }
   }
   private static boolean hashMore(){
      return true;
   }
}

//"tczaflw12345~"
//redis-trib.rb  create  --replicas  1  192.168.240.131:7000 192.168.240.131:7001  192.168.240.131:7002 192.168.240.130:7007  192.168.240.130:7008  192.168.240.130:7009
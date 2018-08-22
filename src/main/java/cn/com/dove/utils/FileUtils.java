package cn.com.dove.utils;

import cn.com.dove.framework.IClassReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by wangdequan on 2018/1/29.
 */
public class   FileUtils {
   private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);
   private static List<String> CLASSPATHS = new ArrayList();

   public static Map<String,Object> filterClass(IClassReader reader, Map<String,Object> classMap) throws Throwable {
      return getClasses("",reader,classMap);
   }
   /**
    * 扫描配置包下的所有的类
    * @param basePack
    * @throws ClassNotFoundException
    */
   public static Map<String,Object> getClasses(String basePack,IClassReader reader, Map<String,Object> classMap) throws Throwable {
      try {
         CLASSPATHS.clear();
         String classpath = FileUtils.class.getResource("/").getPath();
         basePack = basePack.replace(".", File.separator);
         String searchPath = classpath+basePack;
         doPath(new File(searchPath));

         for (String s : CLASSPATHS) {
            s = s.replace(classpath.replace("/", "\\").replaceFirst("\\\\", ""), "").replace("\\", ".").replace(".class", "");
            Class cls =  Class.forName(s);
            if(!cls.isInterface() && !cls.isAnnotation() && !cls.isEnum()){
               reader.read(cls,classMap);
            }

         }
      } catch (ClassNotFoundException e) {
         e.printStackTrace();
      }
      return classMap;
   }

   /**
    * 递归出实体类的路径
    * @param file
    */
   private static void doPath(File file) {

      if (file.isDirectory()) {
         File[] files = file.listFiles();
         for (File f : files) {
            doPath(f);
         }
      } else {
         if (file.getName().endsWith(".class")) {
            CLASSPATHS.add(file.getPath());
         }
      }
   }
   public static void main(String argsp[]){
   }
}

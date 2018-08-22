package cn.com.dove.framework;

import cn.com.dove.framework.context.ApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;

/**
 * Created by wangdequan on 2018/1/29.
 */
public class JDoveContextLoader {

   private static final Logger logger = LoggerFactory.getLogger(JDoveContextLoader.class);

   public void initWebContainer(ServletContextEvent sc, ApplicationContext context){

      try{

         //load dataBase
         context.registerDataBase();

         context.beforeCreateInvoke();

         String packageName = sc.getServletContext().getInitParameter("controllers");

         //register all controllers
         context.registerAppContext(packageName);

         //register autoInject class
         context.registerAutoInject();

         context.afterCreateInvoke();

         //set applicationContext in memory
         ApplicationFactory.setApplicationContext(context);

      }catch (Exception e){
          e.printStackTrace();
      } catch (Throwable throwable) {
         throwable.printStackTrace();
      }
   }

}

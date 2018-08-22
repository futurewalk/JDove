package cn.com.dove.framework;

import cn.com.dove.framework.context.impl.ApplicationContextImpl;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by wangdequan on 2018/1/29.
 */
public class JDoveContextListener extends JDoveContextLoader implements ServletContextListener {
   @Override
   public void contextInitialized(ServletContextEvent servletContextEvent) {
      super.initWebContainer(servletContextEvent,new ApplicationContextImpl());
   }

   @Override
   public void contextDestroyed(ServletContextEvent servletContextEvent) {
      ApplicationFactory.getHolerMap().clear();
   }
}

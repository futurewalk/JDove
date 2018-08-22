package cn.com.dove.framework.context;

import java.lang.reflect.Method;

public interface AOPContextService {

   /**
    * method invoke before
    */
   <T> void registerBefore(String[] pathName,Method entryPoit) throws Throwable;

   /**
    * register after controller
    * @param pathName
    * @param <T>
    */
   <T> void registerAfter(String[] pathName,Method entryPoit);

   /**
    * register around controller
    * @param pathName
    * @param <T>
    */
   <T> void registerAround(String[] pathName,Method entryPoit);

   /**
    * register around controller
    * @param pathName
    * @param <T>
    */
   <T> void registerThrowAfter(String[] pathName,Method entryPoit);


   void registerAspect() throws Throwable;


}
package cn.com.dove.framework;


import cn.com.dove.anotation.Aspect;
import cn.com.dove.anotation.Before;

import java.lang.reflect.Method;

@Aspect
public class Aop{

   @Before(ctrlPath = "cn.com.dove.controller.*")
   public  void testAspect(){


   }
    @Before(ctrlMethod = "cn.com.dove.controller.UserController.getUserPageList")
    public  void testmethod(){


    }

//    @Before(clazz = UserController.class)
    public  void testClass(){


    }
   public static void main(String argsp[]){
       Class clazz = Aop.class;
       Aspect aspect = (Aspect) clazz.getAnnotation(Aspect.class);

       Method[] methods = clazz.getMethods();

       for (int i = 0; i < methods.length; i++) {
           Method method = methods[i];
           Before before = method.getAnnotation(Before.class);
           if(before != null){
               System.out.println(methods[i]);
           }
       }

       System.out.println(aspect);

   }
}
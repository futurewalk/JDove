package cn.com.dove.framework;

import cn.com.dove.framework.orm.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by wangdequan on 2018/2/2.
 */
public abstract class FrameworkServlet extends HttpServlet {
   private static final String favicon_ico = "/favicon.ico";
   private static final Logger logger = LoggerFactory.getLogger(FrameworkServlet.class);


   @Override
   protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      try{
         if(!favicon_ico.equals(req.getRequestURI())){
            dealRequest(req,resp);
         }
      }catch(Exception e){
          e.printStackTrace();
      }finally {
         if(!favicon_ico.equals(req.getRequestURI())){
            SessionFactory.release();

         }
      }
   }

   protected abstract void dealRequest(HttpServletRequest req, HttpServletResponse resp)  throws ServletException, IOException ;

}

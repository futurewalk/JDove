package cn.com.dove.framework;

import cn.com.dove.framework.em.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by wangdequan on 2018/2/2.
 */
public abstract class WebAppServlet extends FrameworkServlet {
   private static final Logger logger = LoggerFactory.getLogger(WebAppServlet.class);

   protected  void dealRequest(HttpServletRequest req, HttpServletResponse resp)  throws ServletException, IOException {
      req.setCharacterEncoding("UTF-8");
      resp.setCharacterEncoding("UTF-8");
      String originHeader=((HttpServletRequest) req).getHeader("Origin");
	  resp.setHeader("Access-Control-Allow-Origin", originHeader);
	  resp.setHeader("Access-Control-Allow-Methods", "OPTIONS,GET,PUT,DELETE");
	  resp.setHeader("Access-Control-Allow-Headers", "Content-type");
	  resp.setHeader("Access-Control-Allow-Headers", "content-type, x-requested-with");
	  resp.setHeader("Access-Control-Allow-Credentials", "true");

      if(!req.getMethod().equals(Action.OPTIONS.name())){
         init(req,resp);
      }
   }


   protected abstract void init(HttpServletRequest req, HttpServletResponse resp)  throws ServletException, IOException ;


}

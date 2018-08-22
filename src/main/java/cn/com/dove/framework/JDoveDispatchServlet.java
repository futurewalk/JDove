package cn.com.dove.framework;

import cn.com.dove.helper.RequestHandler;
import cn.com.dove.helper.RespUtils;
import cn.com.dove.utils.Constans;
import cn.com.dove.utils.StateManager;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * Created by wangdequan on 2018/1/29.
 */
public class JDoveDispatchServlet extends WebAppServlet {
	private static final Logger logger = LoggerFactory.getLogger(JDoveDispatchServlet.class);
	private static final String CONTROLLER_EXCEPTION = "cn.com.dove.exception.ControllerNotFoundException";
	private static final String LOGIN_EXCEPTION = "cn.com.dove.exception.LoginStateException";

	@Override
	protected void init(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		PrintWriter writer = null;
		Map<String,Object> listenerMap = getRequestListener();

		try {

			RequestHandler.checkRequest(req, resp);

			writer = resp.getWriter();

			StateManager.onRequestBefore(listenerMap);

			Object obj = RequestHandler.invoke(req, resp);

			writer.print(JSON.toJSON(RespUtils.success(obj)));

			StateManager.onRequestFinish(listenerMap);

			resp.flushBuffer();

		} catch (InvocationTargetException e) {
			StateManager.onRequestError(listenerMap);

			e.printStackTrace();
			Throwable t = e.getTargetException();
			if (t.getMessage() == null) {
				writer.print(JSON.toJSON(RespUtils.internalServiceError(Constans.INNER_ERROR_MSG)));
			} else {
				writer.print(JSON.toJSON(RespUtils.defineResp(t.getMessage())));
			}
		} catch (Throwable e) {
			if(LOGIN_EXCEPTION.equals(e.getClass().getName())){
				writer.print(JSON.toJSON(RespUtils.loginState(e.getMessage())));
				return;
			}
			StateManager.onRequestError(listenerMap);
			if (CONTROLLER_EXCEPTION.equals(e.getClass().getName())) {
				writer.print(JSON.toJSON(RespUtils.notFound()));
				return;
			}
			writer.print(JSON.toJSON(RespUtils.internalServiceError(e.getMessage())));
			//e.printStackTrace();

		} finally {
			if (writer != null) {
				writer.flush();
				writer.close();
			}
		}
	}

	private static Map<String,Object> getRequestListener() {
		Map<String, Object> listenerMap = (Map<String, Object>) ApplicationFactory.getStateMap().get(AbstractRequestListener.class.getSimpleName());
		return listenerMap;
	}
}

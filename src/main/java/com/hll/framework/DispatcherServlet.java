package com.hll.framework;

import com.hll.framework.bean.Data;
import com.hll.framework.bean.Handler;
import com.hll.framework.bean.Param;
import com.hll.framework.bean.View;
import com.hll.framework.helper.BeanHelper;
import com.hll.framework.helper.ConfigHelper;
import com.hll.framework.helper.ControllerHelper;
import com.hll.framework.util.CodecUtil;
import com.hll.framework.util.JsonUtil;
import com.hll.framework.util.ReflectionUtil;
import com.hll.framework.util.StreamUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hll on 2016/4/13.
 * 请求转发器
 */
@WebServlet(urlPatterns = "/*", loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {

  @Override
  public void init(ServletConfig servletConfig) throws ServletException {
    //初始化相关Helper类
    HelperLoader.init();
    //获取ServletContext对象(用于注册Servlet)
    ServletContext servletContext = servletConfig.getServletContext();
    //注册处理JSP的servlet
    ServletRegistration jspServlet = servletContext.getServletRegistration("jsp");
    jspServlet.addMapping(ConfigHelper.getAppJspPath() + "*");
    //注册处理静态资源的默认Servlet
    ServletRegistration defaultServlet = servletContext.getServletRegistration("default");
    defaultServlet.addMapping(ConfigHelper.getAppAssetPath() + "*");
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    //获取请求方法和请求路径
    String requestMethod = req.getMethod().toLowerCase();
    String requestPath = req.getPathInfo();
    //获取Action处理器
    Handler handler = ControllerHelper.getHandler(requestMethod, requestPath);
    if (handler != null) {
      //获取Controller类及其Bean实例
      Class<?> controllerClass = handler.getControllerClass();
      Object controllerBean = BeanHelper.getBean(controllerClass);
      //创建请求参数对象
      Map<String, Object> paramMap = new HashMap<>();
      Enumeration<String> paramNames = req.getParameterNames();
      while (paramNames.hasMoreElements()) {
        String paramName = paramNames.nextElement();
        String paramValue = req.getParameter(paramName);
        paramMap.put(paramName, paramValue);
      }
      String body = CodecUtil.decodeURL(StreamUtil.getString(req.getInputStream()));
      if (StringUtils.isNotEmpty(body)) {
        String[] params = body.split("&");
        if (ArrayUtils.isNotEmpty(params)) {
          for (String param : params) {
            String[] array = param.split("=");
            if (ArrayUtils.isNotEmpty(array) && array.length == 2) {
              String paramName = array[0];
              String paramValue = array[1];
              paramMap.put(paramName, paramValue);
            }
          }
        }
      }
      Param param = new Param(paramMap);
      //调用Action 方法
      Method actionMethod = handler.getActionMethod();
      Object result = ReflectionUtil.invokeMethod(controllerBean, actionMethod, param);
      //处理Action 方法返回值
      if (result instanceof View) {
        //返回JSP页面
        View view = (View) result;
        String path = view.getPath();
        if (StringUtils.isNotEmpty(path)) {
          if (path.startsWith("/")) {
            resp.sendRedirect(req.getContextPath() + path);
          } else {
            Map<String, Object> model = view.getModel();
            for (Map.Entry<String, Object> entry : model.entrySet()) {
              req.setAttribute(entry.getKey(), entry.getValue());
            }
            req.getRequestDispatcher(ConfigHelper.getAppJspPath() + path).forward(req, resp);
          }
        }
      } else if (result instanceof Data) {
        //返回JSON数据
        Data data = (Data) result;
        Object model = data.getModel();
        if (model != null) {
          resp.setContentType("appliscation/json");
          resp.setCharacterEncoding("UTF-8");
          PrintWriter writer = resp.getWriter();
          String json = JsonUtil.toJson(model);
          writer.write(json);
          writer.flush();
          writer.close();
        }
      }
    }
  }
}

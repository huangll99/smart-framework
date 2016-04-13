package com.hll.framework.helper;

import com.hll.framework.annotation.Action;
import com.hll.framework.bean.Handler;
import com.hll.framework.bean.Request;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by hll on 2016/4/13.
 */
public final class ControllerHelper {

  /**
   * 用于存放请求与处理器的映射关系
   */
  private static final Map<Request, Handler> ACTION_MAP = new HashMap<>();

  static {
    //获取所有的Controller类
    Set<Class<?>> controllerClassSet = ClassHelper.getControllerClassSet();
    if (CollectionUtils.isNotEmpty(controllerClassSet)) {
      for (Class<?> controllerClass : controllerClassSet) {
        Method[] methods = controllerClass.getDeclaredMethods();
        if (ArrayUtils.isNotEmpty(methods)) {
          for (Method method : methods) {
            if (method.isAnnotationPresent(Action.class)) {
              Action action = method.getAnnotation(Action.class);
              String mapping = action.value();
              //验证URL映射规则
              if (mapping.matches("\\w+:/\\w*")) {
                String[] array = mapping.split(":");
                if (ArrayUtils.isNotEmpty(array) && array.length == 2) {
                  //获取请求方法与请求路径
                  String requestMethod = array[0];
                  String requestPath = array[1];
                  Request request = new Request(requestMethod, requestPath);
                  Handler handler = new Handler(controllerClass, method);
                  ACTION_MAP.put(request, handler);
                }
              }
            }
          }
        }
      }
    }
  }

  /**
   * 获取Handler
   */
  public static Handler getHandler(String requestMethod, String requestPath) {
    Request request = new Request(requestMethod, requestPath);
    return ACTION_MAP.get(request);
  }
}

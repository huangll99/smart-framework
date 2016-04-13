package com.hll.framework.bean;

import java.lang.reflect.Method;

/**
 * Created by hll on 2016/4/13.
 * 封装Action信息
 */
public class Handler {
  /**
   * Controller类
   */
  private Class<?> controllerClass;

  /**
   * Action方法
   */
  private Method actionMethod;

  public Handler(Class<?> controllerClass, Method actionMethod) {
    this.controllerClass = controllerClass;
    this.actionMethod = actionMethod;
  }

  public Class<?> getControllerClass() {
    return controllerClass;
  }

  public Method getActionMethod() {
    return actionMethod;
  }
}

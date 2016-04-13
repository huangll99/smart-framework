package com.hll.framework.bean;

/**
 * Created by hll on 2016/4/13.
 * 封装请求信息
 */
public class Request {
  /**
   * 请求方法
   */
  private String requestMethod;

  /**
   * 请求路径
   */
  private String requestPath;

  public Request(String requestMethod, String requestPath) {
    this.requestMethod = requestMethod;
    this.requestPath = requestPath;
  }

  public String getRequestMethod() {
    return requestMethod;
  }

  public String getRequestPath() {
    return requestPath;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Request request = (Request) o;

    if (requestMethod != null ? !requestMethod.equals(request.requestMethod) : request.requestMethod != null)
      return false;
    return requestPath != null ? requestPath.equals(request.requestPath) : request.requestPath == null;

  }

  @Override
  public int hashCode() {
    int result = requestMethod != null ? requestMethod.hashCode() : 0;
    result = 31 * result + (requestPath != null ? requestPath.hashCode() : 0);
    return result;
  }
}

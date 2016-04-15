package com.hll.framework.proxy;

/**
 * Created by hll on 2016/4/14.
 * 代理接口
 */
public interface Proxy {
  /**
   * 执行链式代理
   */
  Object doProxy(ProxyChain proxyChain) throws Throwable;
}

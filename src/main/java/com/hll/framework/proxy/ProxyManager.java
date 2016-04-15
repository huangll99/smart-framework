package com.hll.framework.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

import java.util.List;

/**
 * Created by hll on 2016/4/15.
 * 代理管理器
 */
public class ProxyManager {

  /**
   * 创建代理
   */
  @SuppressWarnings("unchecked")
  public static <T> T createProxy(final Class<?> targetClass, final List<Proxy> proxyList) {
    return (T) Enhancer.create(
        targetClass,
        (MethodInterceptor) (o, method, objects, methodProxy) -> new ProxyChain(targetClass, o, method, methodProxy, objects, proxyList).doProxyChain()
    );
  }
}

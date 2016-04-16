package com.hll.framework.helper;

import com.hll.framework.annotation.Aspect;
import com.hll.framework.annotation.Transaction;
import com.hll.framework.proxy.AspectProxy;
import com.hll.framework.proxy.Proxy;
import com.hll.framework.proxy.ProxyManager;
import com.hll.framework.proxy.TransactionProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * Created by hll on 2016/4/15.
 * 方法拦截助手类
 */
public final class AopHelper {

  private static final Logger LOGGGER = LoggerFactory.getLogger(AopHelper.class);

  static {
    try {
      Map<Class<?>, Set<Class<?>>> proxyMap = createProxyMap();
      Map<Class<?>, List<Proxy>> targetMap = createTargetMap(proxyMap);
      for (Map.Entry<Class<?>, List<Proxy>> targetEntry : targetMap.entrySet()) {
        Class<?> targetClass = targetEntry.getKey();
        List<Proxy> proxyList = targetEntry.getValue();
        Object proxy = ProxyManager.createProxy(targetClass, proxyList);
        BeanHelper.setBean(targetClass, proxy);//最关键的一步，用代理对象代换目标对象
      }
    } catch (Exception e) {
      LOGGGER.error("aop failure", e);
    }
  }

  private static Set<Class<?>> createTargetClassSet(Aspect aspect) {
    Set<Class<?>> targetClassSet = new HashSet<>();
    Class<? extends Annotation> annotation = aspect.value();
    if (annotation != null && !annotation.equals(Aspect.class)) {
      targetClassSet.addAll(ClassHelper.getClassSetByAnnotation(annotation));
    }
    return targetClassSet;
  }

  private static Map<Class<?>, Set<Class<?>>> createProxyMap() throws Exception {
    Map<Class<?>, Set<Class<?>>> proxyMap = new HashMap<>();

    addAspectProxy(proxyMap);
    addTransactionProxy(proxyMap);
    return proxyMap;
  }

  private static void addTransactionProxy(Map<Class<?>, Set<Class<?>>> proxyMap) {
    Set<Class<?>> serviceClassSet = ClassHelper.getServiceClassSet();
    proxyMap.put(TransactionProxy.class,serviceClassSet);
  }

  private static void addAspectProxy(Map<Class<?>, Set<Class<?>>> proxyMap) {
    Set<Class<?>> proxyClassSet = ClassHelper.getClassSetBySuper(AspectProxy.class);
    proxyClassSet.stream().filter(proxyClass -> proxyClass.isAnnotationPresent(Aspect.class)).forEach(proxyClass -> {
      Aspect aspect = proxyClass.getAnnotation(Aspect.class);
      Set<Class<?>> targetClassSet = createTargetClassSet(aspect);
      proxyMap.put(proxyClass, targetClassSet);
    });
  }


  //結果中的目标和代理的映射中，代理的顺序是没有保障的
  private static Map<Class<?>, List<Proxy>> createTargetMap(Map<Class<?>, Set<Class<?>>> proxyMap) throws Exception {
    Map<Class<?>, List<Proxy>> targetMap = new HashMap<>();
    for (Map.Entry<Class<?>, Set<Class<?>>> proxyEntry : proxyMap.entrySet()) {
      Class<?> proxyClass = proxyEntry.getKey();
      Set<Class<?>> targetClassSet = proxyEntry.getValue();
      for (Class<?> targetClass : targetClassSet) {
        Proxy proxy = (Proxy) proxyClass.newInstance();
        if (targetMap.containsKey(targetClass)) {
          targetMap.get(targetClass).add(proxy);
        } else {
          List<Proxy> proxyList = new ArrayList<>();
          proxyList.add(proxy);
          targetMap.put(targetClass, proxyList);
        }
      }
    }
    return targetMap;
  }

}

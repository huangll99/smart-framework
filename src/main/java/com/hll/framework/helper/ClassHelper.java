package com.hll.framework.helper;

import com.hll.framework.annotation.Controller;
import com.hll.framework.annotation.Service;
import com.hll.framework.util.ClassUtil;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by hll on 2016/4/13.
 */
public final class ClassHelper {

  /**
   * 定义类集合（用于存放所加载的类）
   */
  private static final Set<Class<?>> CLASS_SET;

  static {
    String basePackage = ConfigHelper.getAppBasePackage();
    CLASS_SET = ClassUtil.getClassSet(basePackage);
  }

  /**
   * 获取应用包名下的所有类
   */
  public static Set<Class<?>> getClassSet() {
    return CLASS_SET;
  }

  /**
   * 获取应用包名下的所有Service类
   */
  public static Set<Class<?>> getServiceClassSet() {
    return CLASS_SET.stream().filter(cls -> cls.isAnnotationPresent(Service.class)).collect(Collectors.toSet());
  }

  /**
   * 获取应用包名下所有Controller类
   */
  public static Set<Class<?>> getControllerClassSet() {
    return CLASS_SET.stream().filter(cls -> cls.isAnnotationPresent(Controller.class)).collect(Collectors.toSet());
  }

  /**
   * 获取应用包名下所有Bean
   */
  public static Set<Class<?>> getBeanClassSet() {
    Set<Class<?>> classSet = new HashSet<>();
    classSet.addAll(getServiceClassSet());
    classSet.addAll(getControllerClassSet());
    return classSet;
  }

  /**
   * 获取应用包名下某父类（或接口）的所有子类（或实现类）
   */
  public static Set<Class<?>> getClassSetBySuper(Class<?> superClass) {
    return CLASS_SET.stream().filter(cls -> superClass.isAssignableFrom(cls) && !superClass.equals(cls)).collect(Collectors.toSet());
  }

  /**
   * 获取应用包名下带有某注解的所有类
   */
  public static Set<Class<?>> getClassSetByAnnotation(Class<? extends Annotation> annotationClass) {
    return CLASS_SET.stream().filter(cls -> cls.isAnnotationPresent(annotationClass)).collect(Collectors.toSet());
  }

}

package com.hll.framework.annotation;

import java.lang.annotation.*;

/**
 * Created by hll on 2016/4/14.
 * 切面注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Aspect {
  /**
   * 注解
   */
  Class<? extends Annotation> value();
}

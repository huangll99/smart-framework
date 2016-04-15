package com.hll.framework;

import com.hll.framework.helper.*;
import com.hll.framework.util.ClassUtil;

/**
 * Created by hll on 2016/4/13.
 * 加载相应的Helper类
 */
public final class HelperLoader {

  public static void init() {
    Class<?>[] classList = {
        ClassHelper.class,
        BeanHelper.class,
        AopHelper.class,
        IocHelper.class,
        ControllerHelper.class
    };
    for (Class<?> cls : classList) {
      ClassUtil.loadClass(cls.getName(), true);
    }
  }
}

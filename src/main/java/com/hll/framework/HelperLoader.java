package com.hll.framework;

import com.hll.framework.helper.BeanHelper;
import com.hll.framework.helper.ClassHelper;
import com.hll.framework.helper.ControllerHelper;
import com.hll.framework.helper.IocHelper;
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
        IocHelper.class,
        ControllerHelper.class
    };
    for (Class<?> cls : classList) {
      ClassUtil.loadClass(cls.getName(), true);
    }
  }
}

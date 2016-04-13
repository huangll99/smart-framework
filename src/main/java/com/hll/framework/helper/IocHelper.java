package com.hll.framework.helper;

import com.hll.framework.annotation.Inject;
import com.hll.framework.util.ReflectionUtil;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by hll on 2016/4/13.
 */
public final class IocHelper {

  static {
    //获取所有的Bean类和Bean实例之间的映射关系
    Map<Class<?>, Object> beanMap = BeanHelper.getBeanMap();
    if (MapUtils.isNotEmpty(beanMap)) {
      //遍历BeanMap
      for (Map.Entry<Class<?>, Object> beanEntry : beanMap.entrySet()) {
        //从BeanMap中获取Bean类与Bean实例
        Class<?> beanClass = beanEntry.getKey();
        Object beanInstance = beanEntry.getValue();
        //获取Bean类定义的所有成员变量
        Field[] fields = beanClass.getDeclaredFields();
        if (ArrayUtils.isNotEmpty(fields)) {
          for (Field beanField : fields) {
            if (beanField.isAnnotationPresent(Inject.class)) {
              Class<?> beanFieldClass = beanField.getType();
              Object beanFieldInstance = beanMap.get(beanFieldClass);
              if (beanFieldClass != null) {
                ReflectionUtil.setField(beanInstance, beanField, beanFieldInstance);
              }
            }
          }
        }
      }
    }
  }
}

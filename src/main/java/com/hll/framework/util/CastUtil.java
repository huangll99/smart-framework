package com.hll.framework.util;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by hll on 2016/4/11
 * 类型转换工具类
 */
public class CastUtil {
  /**
   * 转换为String型
   */
  public static String castString(Object obj) {
    return castString(obj, "");
  }

  /**
   * 转换为String型(可指定默认值)
   */
  public static String castString(Object obj, String defaultValue) {
    return obj != null ? String.valueOf(obj) : defaultValue;
  }

  /**
   * 转换为int型
   */
  public static int castInt(Object obj) {
    return castInt(obj, 0);
  }

  /**
   * 转换为int型(可指定默认值)
   */
  public static int castInt(Object obj, int defaultValue) {
    int intValue = defaultValue;
    if (obj != null) {
      String strValue = castString(obj);
      if (StringUtils.isNoneEmpty(strValue)) {
        try {
          intValue = Integer.parseInt(strValue);
        } catch (NumberFormatException e) {
          intValue = defaultValue;
        }
      }
    }
    return intValue;
  }

  /**
   * 转为boolean型
   */
  public static boolean castBoolean(Object o) {
    return CastUtil.castBoolean(o, false);
  }

  /**
   * 转为boolean型（提供默认值）
   */
  public static boolean castBoolean(Object o, boolean defaultValue) {
    boolean value = defaultValue;
    if (o != null) {
      value = Boolean.parseBoolean(castString(o));
    }
    return value;
  }

  public static long castLong(Object o, long defaultValue) {
    return o == null ? defaultValue : (long) o;
  }

  public static long castLong(Object o) {
    return castLong(o, 0);
  }
}

package com.hll.framework.bean;

/**
 * Created by hll on 2016/4/13.
 * 返回数据对象
 */
public class Data {
  /**
   * 模型数据
   */
  private Object model;

  public Data(Object model) {
    this.model = model;
  }

  public Object getModel() {
    return model;
  }
}
